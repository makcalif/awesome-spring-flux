package com.cognizant.labs.spring.flux.awesomespringflux;

import com.cognizant.labs.spring.flux.awesomespringflux.domain.Address;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Employee;
import com.cognizant.labs.spring.flux.awesomespringflux.domain.Equipment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;

public class AggregateAndFlatMapTest {

    @Test
    public void sample() {
        Flux<Long> range = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.create(range)
                .expectNext(0L, 1L, 2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void zipAndPrintFlux() {
        Flux<String> first = Flux.fromIterable(Arrays.asList("A", "B", "C"));

        Flux<String> second = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> zip = Flux.zip(first, second)
                .map((f) -> f.getT1() + f.getT2());

        zip.doOnNext(System.out::println)
                .blockLast();
    }


    @Test
    public void zipAndPrintComplexFlux() {

        Flux<Employee> employeeBasic = Flux.fromIterable(Arrays.asList(
                new Employee("123", "John", "Doe"),
                new Employee("234", "Michael", "Phelps"),
                new Employee("234", "Andre", "Aggasi")
        ));

        Flux<Long> employeeDelay = Flux.interval(Duration.ofSeconds(3));
        Flux<Employee> employees = Flux.zip(employeeDelay, employeeBasic)
                .map(e -> e.getT2());

        Flux<Equipment> equipmentBasic = Flux.fromIterable(Arrays.asList(
                new Equipment(12L, "computer", "hardware"),
                new Equipment(12L, "laptop", "hardware"),
                new Equipment(12L, "windows", "software")
        ));

        //Flux<Long> equipmentDelay = randomDelayFlux();
        Flux<Long> equipmentDelay = Flux.interval(Duration.ofSeconds(1));
        Flux<Equipment> equipment = Flux.zip(equipmentDelay, equipmentBasic)
                .map(e -> e.getT2());

        Function<Employee, Flux<String>> mapper = e -> Flux.just(e.toString());
        Flux<String> empStrFlux = employees.flatMap(mapper);

        Function<Equipment, Flux<String>> eqMapper = e -> Flux.just(e.toString());
        Flux<String> equipStrFlux = equipment.flatMap(eqMapper);

        Flux<String> merged = Flux.merge(empStrFlux, equipStrFlux);
        merged.doOnNext(System.out::println)
                .blockLast();

    }

    public Flux<Mono<Long>> randomDelayFlux2() {
        Mono<Long> m1 = getRandomDelayMono();
        Mono<Long> m2 = getRandomDelayMono();
        Mono<Long> m3 = getRandomDelayMono();
        Flux<Mono<Long>> f = Flux.just(m1, m2, m3);
        //f.doOnNext(System.out::println).blockLast();
        return f;
    }


    private Flux<Long> randomDelayFlux() {
        ArrayList<Flux<Long>> fluxList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fluxList.add(Flux.create((FluxSink<Long> sink) -> {
                sink.next(getRandomDelayMono().block());
                sink.complete();
            }));
        }

        Flux<Long> longFlux = Flux.create((FluxSink<Long> sink) -> {
            sink.next(getRandomDelayMono().block());
            sink.complete();
        });

        //return longFlux;
        return Flux.mergeSequential(fluxList);
    }

    @Test
    public void testRandom() {
        randomDelayFlux().doOnNext(System.out::println)
                .blockLast();
    }

    /*
    //private Flux<Long> randomDelayFlux() {
//        return Flux.create(sink -> {
//            sink.next(getRandomDelayMono());
//        });

        Flux<Mono<Long>> created  = Flux.create(sink -> {
           sink.next(getRandomDelayMono());
        });
        return created;

    }*/


    private Mono<Long> getRandomDelayMono() {
        int random = ThreadLocalRandom.current().nextInt(1, 10);
        System.out.println("delay:" + random);
        Mono<Long> delay = Mono.delay(Duration.ofSeconds(random)).then(Mono.just(1L));
        return delay;
    }

    private Flux<Equipment> getEmployeeEquipment(Employee employee) {
        Flux<Equipment> equipmentBasic = Flux.fromIterable(Arrays.asList(
                new Equipment(12L, "computer", "hardware"),
                new Equipment(22L, "laptop", "hardware"),
                new Equipment(33L, "windows", "software")
        ));

        //Flux<Long> equipmentDelay = randomDelayFlux();
        Flux<Long> equipmentDelay = Flux.interval(Duration.ofSeconds(1));
        Flux<Equipment> equipment = Flux.zip(equipmentDelay, equipmentBasic)
                .map(e -> e.getT2());

        return equipment;
    }

    private Flux<Address> getEmployeeAddress(Employee employee) {
        Flux<Address> equipmentBasic = Flux.fromIterable(Arrays.asList(
                new Address(4L, "home", "2344", "folsom street"),
                new Address(6L, "work", "2233", "main street")
        ));

        Flux<Long> equipmentDelay = Flux.interval(Duration.ofSeconds(1));
        Flux<Address> addresses = Flux.zip(equipmentDelay, equipmentBasic)
                .map(e -> e.getT2());

        return addresses;
    }

    @Test
    public void nestedFlagMap() {
        Flux<Equipment> equipmentBasic = Flux.fromIterable(Arrays.asList(
                new Equipment(12L, "computer", "hardware"),
                new Equipment(12L, "laptop", "hardware"),
                new Equipment(12L, "windows", "software")
        ));

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("address1", "123 some street");
        addressMap.put("address2", "54 Folsom Rd");
        Flux<Map<String, String>> addressFlux = Flux.just(addressMap);


        Flux<List<String>> flatAddress = addressFlux.flatMap(address -> {
            List<String> list = new ArrayList(address.values());
            return Flux.just(list);
        });

        Flux<String> equipFlat = equipmentBasic.flatMap(equipment -> {
            return Flux.just(equipment.getName());
        });

        equipmentBasic.flatMap(eq -> {

            Flux<String> equipFlux = Flux.just(eq.getName());

            Flux<List<String>> addListFlux = flatAddress.flatMap(address -> {
                return Flux.just(address);
            });
            Flux<String> zipped = equipFlux.zipWith(addListFlux)
                    .flatMap((a) -> {
                        return Flux.just(a.getT1() + a.getT2());
                    });

            return zipped;
        }).doOnNext(l -> {
            System.out.println("line:" + l);
        })
                .blockLast();
    }

    @Test
    public void chunksOfEmployee_and_zip_equipment_and_addresses() {

        Flux<Employee> employeeBasic = Flux.fromIterable(Arrays.asList(
                new Employee("123", "John", "Doe"),
                new Employee("234", "Michael", "Phelps"),
                new Employee("345", "Andre", "Aggasi")
        ));

        Flux<Long> employeeDelay = Flux.interval(Duration.ofSeconds(1));
        Flux<Employee> employees = Flux.zip(employeeDelay, employeeBasic)
                .map(e -> e.getT2());

        employees
            // equivalent of map and merge
            .flatMap(emp -> {
                // get equipment

                Flux<Equipment> equip = this.getEmployeeEquipment(emp);
                Flux<Address> address = this.getEmployeeAddress(emp);

                Mono<List<String>> equipList = equip.
                        flatMap(e -> Flux.just(e.toString()))
                        .collectList();
                Mono<List<String>> addressList = address.
                        flatMap(e -> Flux.just(e.toString()))
                        .collectList();

                return Flux.zip(equipList, addressList)
                        .flatMap((all) -> Flux.just(emp.getId() + ", " + emp.getFirstName()
                                + all.getT1() +":" + all.getT2()));
            })
            .doOnNext(System.out::println)
            .blockLast();
    }
}