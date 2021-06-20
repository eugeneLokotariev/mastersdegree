package com.mastersessay.blockchain.accounting.util;

import com.mastersessay.blockchain.accounting.consts.AirConditioningDeviceType;
import com.mastersessay.blockchain.accounting.consts.OrderDevicePurpose;
import com.mastersessay.blockchain.accounting.consts.OrderStatus;
import com.mastersessay.blockchain.accounting.consts.OrderType;
import com.mastersessay.blockchain.accounting.model.dictionary.Manufacturer;
import com.mastersessay.blockchain.accounting.model.dictionary.facility.*;
import com.mastersessay.blockchain.accounting.model.order.*;
import com.mastersessay.blockchain.accounting.model.user.Role;
import com.mastersessay.blockchain.accounting.model.user.User;
import com.mastersessay.blockchain.accounting.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

import static com.mastersessay.blockchain.accounting.consts.UserRole.*;

@Service
@SuppressWarnings({
        "OptionalGetWithoutIsPresent",
        "Duplicates"
})
@Profile("dev")
public class OnStartupDatabaseTestDataRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MiningFarmRepository miningFarmRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private AirConditioningDeviceRepository airConditioningDeviceRepository;
    @Autowired
    private AirHandlingUnitRepository airHandlingUnitRepository;
    @Autowired
    private FanRepository fanRepository;
    @Autowired
    private MiningCoolingRackRepository miningCoolingRackRepository;

    @Autowired
    private OrderAirConditioningDeviceRepository orderAirConditioningDeviceRepository;
    @Autowired
    private OrderAirHandlingUnitRepository orderAirHandlingUnitRepository;
    @Autowired
    private OrderFanRepository orderFanRepository;
    @Autowired
    private OrderMiningCoolingRackRepository orderMiningCoolingRackRepository;
    @Autowired
    private OrderMiningFarmRepository orderMiningFarmRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrdersActionHistoryRepository ordersActionHistoryRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        saveUserInfo();
        saveMiningFarms();
        saveCoolingDevices();
        saveOrders();
    }

    @Transactional
    public void saveUserInfo() {
        persistRoles();
        persistUsers();
    }

    @Transactional
    public void saveMiningFarms() {
        persistMiningFarmManufacturers();
        persistMiningFarms();
    }

    @Transactional
    public void saveCoolingDevices() {
        persistAirConditioningDevices();
        persistAirHandlingDevices();
        persistFanDevices();
        persistMiningCoolingDevices();
    }

    @Transactional
    public void saveOrders() {
        persistOrders();
    }

    private void persistRoles() {
        roleRepository
                .save(Role
                        .builder()
                        .name(ROLE_CATALOG_ADMIN)
                        .build());
        roleRepository
                .save(Role
                        .builder()
                        .name(ROLE_ORDER_ADMIN)
                        .build());
        roleRepository
                .save(Role
                        .builder()
                        .name(ROLE_MAINTENANCE_ADMIN)
                        .build());
        roleRepository
                .save(Role
                        .builder()
                        .name(ROLE_USER_ADMIN)
                        .build());
        roleRepository
                .save(Role
                        .builder()
                        .name(ROLE_USER_PLAIN)
                        .build());
    }

    private void persistUsers() {
        HashSet<Role> role1 = new HashSet<Role>() {{
            add(roleRepository.findByName(ROLE_CATALOG_ADMIN).get());
        }};
        HashSet<Role> role2 = new HashSet<Role>() {{
            add(roleRepository.findByName(ROLE_ORDER_ADMIN).get());
        }};
        HashSet<Role> role3 = new HashSet<Role>() {{
            add(roleRepository.findByName(ROLE_USER_ADMIN).get());
        }};
        HashSet<Role> role4 = new HashSet<Role>() {{
            add(roleRepository.findByName(ROLE_MAINTENANCE_ADMIN).get());
        }};
        HashSet<Role> role5 = new HashSet<Role>() {{
            add(roleRepository.findByName(ROLE_USER_PLAIN).get());
        }};

        userRepository
                .save(User
                        .builder()
                        .username("catalogAdmin")
                        .password(encoder.encode("catalogAdmin"))
                        .email("catalogAdmin@testmail.com")
                        .roles(role1)
                        .isEnabled(true)
                        .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                        .build());
        userRepository
                .save(User
                        .builder()
                        .username("orderAdmin")
                        .password(encoder.encode("orderAdmin"))
                        .email("orderAdmin@testmail.com")
                        .roles(role2)
                        .isEnabled(true)
                        .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                        .build());
        userRepository
                .save(User
                        .builder()
                        .username("userAdmin")
                        .password(encoder.encode("userAdmin"))
                        .email("userAdmin@testmail.com")
                        .roles(role3)
                        .isEnabled(true)
                        .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                        .build());
        userRepository
                .save(User
                        .builder()
                        .username("maintenanceAdmin")
                        .password(encoder.encode("maintenanceAdmin"))
                        .email("maintenanceAdmin@testmail.com")
                        .roles(role4)
                        .isEnabled(true)
                        .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                        .build());
        userRepository
                .save(User
                        .builder()
                        .username("userPlain")
                        .password(encoder.encode("userPlain"))
                        .email("userPlain@testmail.com")
                        .roles(role5)
                        .isEnabled(true)
                        .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                        .build());
    }

    private void persistMiningFarmManufacturers() {
        Manufacturer goldshell = Manufacturer
                .builder()
                .name("Goldshell")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer innosilicon = Manufacturer
                .builder()
                .name("Innosilicon")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer bitmain = Manufacturer
                .builder()
                .name("Bitmain")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer strongU = Manufacturer
                .builder()
                .name("StrongU")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer spondoolies = Manufacturer
                .builder()
                .name("Spondoolies")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer microBT = Manufacturer
                .builder()
                .name("MicroBT")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .modifiedBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .modifiedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer mitsubishi = Manufacturer
                .builder()
                .name("Mitsubishi")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer behtc = Manufacturer
                .builder()
                .name("ВЕНТС")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer soler = Manufacturer
                .builder()
                .name("Soler&Palau")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer dospel = Manufacturer
                .builder()
                .name("Dospel")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer aspira = Manufacturer
                .builder()
                .name("ASPIRA")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        Manufacturer antminer = Manufacturer
                .builder()
                .name("Antminer S17")
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();

        manufacturerRepository.save(goldshell);
        manufacturerRepository.save(innosilicon);
        manufacturerRepository.save(bitmain);
        manufacturerRepository.save(strongU);
        manufacturerRepository.save(spondoolies);
        manufacturerRepository.save(microBT);
        manufacturerRepository.save(mitsubishi);
        manufacturerRepository.save(behtc);
        manufacturerRepository.save(soler);
        manufacturerRepository.save(dospel);
        manufacturerRepository.save(aspira);
        manufacturerRepository.save(antminer);
    }

    private void persistMiningFarms() {
        MiningFarm farm1 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Innosilicon").get())
                .model("A11 Pro ETH (2000Mh)")
                .alsoAsKnownAs("A11 Pro ETHMiner 8G 2000Mh")
                .releaseDate(YearMonth.of(2021, 7).toString())
                .noiseLevel("75db")
                .power("2500W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .memory("8Gb")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(700)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm2 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Goldshell").get())
                .model("KD5")
                .alsoAsKnownAs("KD5 Kadena miner")
                .releaseDate(YearMonth.of(2021, 3).toString())
                .size("200 x 264 x 290mm")
                .weight("8500g")
                .noiseLevel("80db")
                .fans(2)
                .power("2250W")
                .voltage("176~264V")
                .interfaceName("Ethernet")
                .memory("8Gb")
                .temperature("5 - 35 °C")
                .humidity("5 - 95 %")
                .priceUsd(400)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm3 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Innosilicon").get())
                .model("A10 Pro+ ETH (750Mh")
                .alsoAsKnownAs("A10 PRO-S 7GB ETHMiner")
                .releaseDate(YearMonth.of(2020, 12).toString())
                .size("136 x 285 x 362mm")
                .weight("8100g")
                .fans(2)
                .noiseLevel("75db")
                .power("1350W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .memory("7Gb")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(1000)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm4 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Goldshell").get())
                .model("HS5")
                .releaseDate(YearMonth.of(2021, 2).toString())
                .size("264 x 200 x 290mm")
                .weight("8500g")
                .fans(4)
                .noiseLevel("80db")
                .power("2650W")
                .voltage("176~264V")
                .memory("7Gb")
                .temperature("5 - 35 °C")
                .humidity("5 - 95 %")
                .priceUsd(1500)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm5 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Innosilicon").get())
                .model("A10 Pro ETH (500Mh)")
                .alsoAsKnownAs("ETH Miner King A10 Pro")
                .releaseDate(YearMonth.of(2020, 3).toString())
                .size("136 x 285 x 362mm")
                .weight("8100g")
                .fans(2)
                .noiseLevel("75db")
                .power("960W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .memory("6Gb")
                .temperature("5 - 40 °C")
                .humidity("5 - 95 %")
                .priceUsd(1450)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm6 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Goldshell").get())
                .model("HS3")
                .alsoAsKnownAs("HS3 Handshake miner 2000Gh/s")
                .releaseDate(YearMonth.of(2020, 10).toString())
                .size("180 x 280 x 310mm")
                .weight("7000g")
                .fans(2)
                .noiseLevel("75db")
                .power("2000W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .memory("6Gb")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(1800)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm7 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Bitmain").get())
                .model("Antminer Z15")
                .releaseDate(YearMonth.of(2020, 6).toString())
                .size("133 x 245 x 290mm")
                .weight("9000g")
                .fans(2)
                .noiseLevel("72db")
                .power("1510W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(2000)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm8 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("StrongU").get())
                .model("STU-U1++")
                .alsoAsKnownAs("Miner U1 Plus Plus")
                .releaseDate(YearMonth.of(2019, 7).toString())
                .size("130 x 220 x 390mm")
                .weight("8200g")
                .fans(2)
                .noiseLevel("76db")
                .cooling("12038 fan")
                .power("2200W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(1680)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm9 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Spondoolies").get())
                .model("SPx36")
                .alsoAsKnownAs("Miner U1 Plus Plus")
                .releaseDate(YearMonth.of(2018, 10).toString())
                .size("595 x 434 x 88mm")
                .weight("19500g")
                .chipCount(150)
                .fans(4)
                .noiseLevel("75db")
                .power("4400W")
                .rackFormat("2U")
                .voltage("12V")
                .interfaceName("Ethernet")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(500)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm10 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("Goldshell").get())
                .model("KD2")
                .alsoAsKnownAs("KD2 Kadena miner")
                .releaseDate(YearMonth.of(2021, 3).toString())
                .size("200 x 264 x 290mm")
                .weight("7300g")
                .fans(2)
                .noiseLevel("55db")
                .power("830W")
                .voltage("176~264V")
                .interfaceName("Ethernet")
                .temperature("5 - 45 °C")
                .humidity("5 - 95 %")
                .priceUsd(800)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();
        MiningFarm farm11 = MiningFarm
                .builder()
                .manufacturer(manufacturerRepository.getByName("MicroBT").get())
                .model("Whatsminer D1")
                .alsoAsKnownAs("Whatsminer DCR 48Th/s, D10V1")
                .releaseDate(YearMonth.of(2018, 11).toString())
                .size("130 x 220 x 390mm")
                .weight("8500g")
                .chipCount(315)
                .fans(2)
                .noiseLevel("75db")
                .power("2200W")
                .voltage("12V")
                .interfaceName("Ethernet")
                .temperature("5 - 45 °C")
                .priceUsd(1250)
                .createdBy(userRepository.findByUsername("catalogAdmin").get().getUsername())
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .build();

        miningFarmRepository.save(farm1);
        miningFarmRepository.save(farm2);
        miningFarmRepository.save(farm3);
        miningFarmRepository.save(farm4);
        miningFarmRepository.save(farm5);
        miningFarmRepository.save(farm6);
        miningFarmRepository.save(farm7);
        miningFarmRepository.save(farm8);
        miningFarmRepository.save(farm9);
        miningFarmRepository.save(farm10);
        miningFarmRepository.save(farm11);
    }

    private void persistAirConditioningDevices() {
        String catalogAdmin = userRepository.findByUsername("catalogAdmin").get().getUsername();

        AirConditioningDevice device1 = AirConditioningDevice
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("MSZ-FH35VE/MUZ-FH35VE")
                .manufacturer(manufacturerRepository.getByName("Mitsubishi").get())
                .noiseLevel("61 dB")
                .power("3.5 kWt")
                .voltage("220 V")
                .weight("13,5 kg")
                .priceUsd(1321)
                .size("30.5х92.5х23.4 sm")
                .airConditioningDeviceType(AirConditioningDeviceType.SPLIT_SYSTEM)
                .coolingCapacity("12000 BTE/h")
                .webReference("https://bt.rozetka.com.ua/mitsubishi_electric_msz-fh35ve_muz-fh35ve/p381980/")
                .roomAreaSquareM(35)
                .build();
        AirConditioningDevice device2 = AirConditioningDevice
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("SEZ-M35DA/SUZ-KA35VA")
                .manufacturer(manufacturerRepository.getByName("Mitsubishi").get())
                .noiseLevel("23 dB")
                .power("1130 kWt")
                .voltage("220 V")
                .weight("21 kg")
                .priceUsd(2300)
                .size("99×70×20 sm")
                .airConditioningDeviceType(AirConditioningDeviceType.CANAL)
                .coolingCapacity("12000 BTE/h")
                .webReference("https://bt.rozetka.com.ua/248450467/p248450467/")
                .roomAreaSquareM(35)
                .build();
        AirConditioningDevice device3 = AirConditioningDevice
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("SEZ-M25DA/SUZ-KA25V")
                .manufacturer(manufacturerRepository.getByName("Mitsubishi").get())
                .noiseLevel("23 dB")
                .power("1130 kWt")
                .voltage("220 V")
                .weight("21 kg")
                .priceUsd(2000)
                .airConditioningDeviceType(AirConditioningDeviceType.SPLIT_SYSTEM)
                .coolingCapacity("12000 BTE/h")
                .size("79×70×20 sm")
                .webReference("https://bt.rozetka.com.ua/248450455/p248450455/")
                .roomAreaSquareM(25)
                .build();

        airConditioningDeviceRepository.save(device1);
        airConditioningDeviceRepository.save(device2);
        airConditioningDeviceRepository.save(device3);
    }

    private void persistAirHandlingDevices() {
        String catalogAdmin = userRepository.findByUsername("catalogAdmin").get().getUsername();

        AirHandlingUnit device1 = AirHandlingUnit
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("ECOCOMFORT 160 RF (AP19981)")
                .manufacturer(manufacturerRepository.getByName("ASPIRA").get())
                .noiseLevel("54 dB")
                .power("295 Wt")
                .voltage("220 V")
                .size("16 х 24-58 х 16 sm")
                .weight("3,5 kg")
                .pipeDiameter("160 mm")
                .ventilatedArea("2.52")
                .performance("<100 m3")
                .priceUsd(288)
                .webReference("https://bt.rozetka.com.ua/aspira_ecocomfort_160_rf_ap19981/p257304131/")
                .build();

        airHandlingUnitRepository.save(device1);
    }

    private void persistFanDevices() {
        String catalogAdmin = userRepository.findByUsername("catalogAdmin").get().getUsername();

        Fan fan1 = Fan
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("ВКМС 315")
                .manufacturer(manufacturerRepository.getByName("ВЕНТС").get())
                .airConsumption("1920 м3/h")
                .branchPipeSize("315 mm")
                .noiseLevel("54 dB")
                .power("295 Wt")
                .currentConsumption("1,34 A")
                .voltage("220 V")
                .weight("8,8 kg")
                .priceUsd(288)
                .webReference("https://vent-a.com.ua/pr8-vents-vkms-315/")
                .build();
        Fan fan2 = Fan
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("TD-500/150-160 SILENT")
                .manufacturer(manufacturerRepository.getByName("Soler&Palau").get())
                .airConsumption("550 м3/h")
                .branchPipeSize("150/160 mm")
                .noiseLevel("27 dB")
                .power("59 Wt")
                .currentConsumption("0,26 A")
                .voltage("220 V")
                .weight("6 kg")
                .priceUsd(298)
                .webReference("https://vent-a.com.ua/pr3960-solerpalau-td-500150-160-silent/")
                .build();
        Fan fan3 = Fan
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("ОВ 2Е 200")
                .manufacturer(manufacturerRepository.getByName("ВЕНТС").get())
                .airConsumption("860 м3/h")
                .noiseLevel("50 dB")
                .power("55 Wt")
                .currentConsumption("0.26 A")
                .voltage("220 V")
                .weight("3.9 kg")
                .priceUsd(131)
                .webReference("https://vent-a.com.ua/pr295-vents-ov-2e-200/")
                .build();
        Fan fan4 = Fan
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("WOKS 300")
                .manufacturer(manufacturerRepository.getByName("Dospel").get())
                .airConsumption("2400 м3/h")
                .noiseLevel("73 dB")
                .power("145 Wt")
                .currentConsumption("0.62 A")
                .voltage("220 V")
                .weight("3.0 kg")
                .priceUsd(88)
                .build();

        fanRepository.save(fan1);
        fanRepository.save(fan2);
        fanRepository.save(fan3);
        fanRepository.save(fan4);
    }

    private void persistMiningCoolingDevices() {
        @NotBlank @Size(max = 256) String catalogAdmin = userRepository.findByUsername("catalogAdmin").get().getUsername();
        Manufacturer antminer_s17 = manufacturerRepository.getByName("Antminer S17").get();

        MiningCoolingRack device1 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 3")
                .manufacturer(antminer_s17)
                .waterCapacity(60)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("12 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(1800)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-3-antminer-s17/")
                .build();
        MiningCoolingRack device2 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 3 without body kit")
                .manufacturer(antminer_s17)
                .waterCapacity(60)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("12 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(400)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-3-antminer-s17-bez-obvesa/")
                .build();
        MiningCoolingRack device3 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 3 Air-cooled Dry-Cooler")
                .manufacturer(antminer_s17)
                .waterCapacity(60)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("12 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(1800)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-3-antminer-s17-s-vozdushnym-ohlazhdeniem-dry-cooler/")
                .build();
        MiningCoolingRack device4 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 6")
                .manufacturer(antminer_s17)
                .waterCapacity(120)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("25 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(2200)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-6-antminer-s17/")
                .build();
        MiningCoolingRack device5 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 6 without body kit")
                .manufacturer(antminer_s17)
                .waterCapacity(120)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("25 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(600)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-6-antminer-s17-bez-obvesa/")
                .build();
        MiningCoolingRack device6 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 12 Air-cooled Dry-Cooler")
                .manufacturer(antminer_s17)
                .waterCapacity(120)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("25 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(null)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-6-antminer-s17-s-vozdushnym-ohlazhdeniem-dry-cooler/")
                .build();
        MiningCoolingRack device7 = MiningCoolingRack
                .builder()
                .createdBy(catalogAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .model("For 12")
                .manufacturer(antminer_s17)
                .waterCapacity(120)
                .size("800х600х900")
                .weight("33kg")
                .optimalWaterConsumption("0,25м3/h")
                .maxCoolingPower("25 kWt")
                .pumpConsumption("0,180 kWt")
                .priceUsd(null)
                .webReference("https://simplex-group.com.ua/product/ustanovka-dlya-mayninga-na-12-antminer-s17/")
                .build();

        miningCoolingRackRepository.save(device1);
        miningCoolingRackRepository.save(device2);
        miningCoolingRackRepository.save(device3);
        miningCoolingRackRepository.save(device4);
        miningCoolingRackRepository.save(device5);
        miningCoolingRackRepository.save(device6);
        miningCoolingRackRepository.save(device7);
    }

    private void persistOrders() {
        String orderAdmin = userRepository.findByUsername("orderAdmin").get().getUsername();

        Order order1 = Order
                .builder()
                .createdBy(orderAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .status(OrderStatus.PLANNED)
                .orderType(OrderType.INSTALLATION)
                .waitingActionUsername(orderAdmin)
                .build();
        Order order2 = Order
                .builder()
                .createdBy(orderAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .status(OrderStatus.CANCELLED)
                .orderType(OrderType.MAINTENANCE)
                .build();
        Order order3 = Order
                .builder()
                .createdBy(orderAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .status(OrderStatus.COMPLETED)
                .orderType(OrderType.INSTALLATION)
                .build();
        Order order4 = Order
                .builder()
                .createdBy(orderAdmin)
                .createdWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .status(OrderStatus.PLANNED)
                .orderType(OrderType.INSTALLATION)
                .waitingActionUsername(orderAdmin)
                .build();

        OrderMiningFarm orderMiningFarm1 = OrderMiningFarm
                .builder()
                .miningFarm(miningFarmRepository.getById(1L).get())
                .amount(1)
                .order(order1)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();
        OrderMiningFarm orderMiningFarm2 = OrderMiningFarm
                .builder()
                .miningFarm(miningFarmRepository.getById(2L).get())
                .amount(1)
                .order(order2)
                .orderDevicePurpose(OrderDevicePurpose.MAINTENANCE)
                .build();
        OrderMiningFarm orderMiningFarm3 = OrderMiningFarm
                .builder()
                .miningFarm(miningFarmRepository.getById(3L).get())
                .amount(5)
                .order(order3)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderAirConditioningDevice orderAirConditioningDevice1 = OrderAirConditioningDevice
                .builder()
                .airConditioningDevice(airConditioningDeviceRepository.getById(1L).get())
                .amount(1)
                .order(order1)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderAirConditioningDevice orderAirConditioningDevice2 = OrderAirConditioningDevice
                .builder()
                .airConditioningDevice(airConditioningDeviceRepository.getById(1L).get())
                .amount(1)
                .order(order2)
                .orderDevicePurpose(OrderDevicePurpose.MAINTENANCE)
                .build();

        OrderAirConditioningDevice orderAirConditioningDevice3 = OrderAirConditioningDevice
                .builder()
                .airConditioningDevice(airConditioningDeviceRepository.getById(3L).get())
                .amount(3)
                .order(order3)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderFan orderFan1 = OrderFan
                .builder()
                .fan(fanRepository.getById(1L).get())
                .amount(1)
                .order(order1)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderFan orderFan2 = OrderFan
                .builder()
                .fan(fanRepository.getById(1L).get())
                .amount(1)
                .orderDevicePurpose(OrderDevicePurpose.MAINTENANCE)
                .order(order2)
                .build();

        OrderFan orderFan3 = OrderFan
                .builder()
                .fan(fanRepository.getById(3L).get())
                .amount(8)
                .order(order3)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderAirHandlingUnit orderAirHandlingUnit1 = OrderAirHandlingUnit
                .builder()
                .airHandlingUnit(airHandlingUnitRepository.getById(1L).get())
                .amount(1)
                .order(order1)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrderAirHandlingUnit orderAirHandlingUnit2 = OrderAirHandlingUnit
                .builder()
                .airHandlingUnit(airHandlingUnitRepository.getById(1L).get())
                .amount(8)
                .order(order3)
                .orderDevicePurpose(OrderDevicePurpose.INSTALLATION)
                .build();

        OrdersActionHistory ordersActionHistory1 = OrdersActionHistory
                .builder()
                .order(order1)
                .statusFrom(null)
                .statusTo(OrderStatus.PLANNED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("empty -> planned")
                .build();




        OrdersActionHistory ordersActionHistory2 = OrdersActionHistory
                .builder()
                .order(order2)
                .statusFrom(null)
                .statusTo(OrderStatus.PLANNED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("empty -> planned")
                .build();

        OrdersActionHistory ordersActionHistory3 = OrdersActionHistory
                .builder()
                .order(order2)
                .statusFrom(OrderStatus.PLANNED)
                .statusTo(OrderStatus.IN_PROGRESS)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("planned -> in progress")
                .build();

        OrdersActionHistory ordersActionHistory4 = OrdersActionHistory
                .builder()
                .order(order2)
                .statusFrom(OrderStatus.IN_PROGRESS)
                .statusTo(OrderStatus.CANCELLED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("in progress -> cancelled")
                .build();




        OrdersActionHistory ordersActionHistory5 = OrdersActionHistory
                .builder()
                .order(order3)
                .statusFrom(null)
                .statusTo(OrderStatus.PLANNED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("empty -> planned")
                .build();

        OrdersActionHistory ordersActionHistory6 = OrdersActionHistory
                .builder()
                .order(order3)
                .statusFrom(OrderStatus.PLANNED)
                .statusTo(OrderStatus.IN_PROGRESS)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("planned -> in progress")
                .build();

        OrdersActionHistory ordersActionHistory7 = OrdersActionHistory
                .builder()
                .order(order3)
                .statusFrom(OrderStatus.IN_PROGRESS)
                .statusTo(OrderStatus.COMPLETED)
                .actionExecutingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")))
                .actionExecutionUsername(orderAdmin)
                .actionComment("in progress -> completed")
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);

        orderFanRepository.save(orderFan1);
        orderFanRepository.save(orderFan2);
        orderFanRepository.save(orderFan3);

        orderAirHandlingUnitRepository.save(orderAirHandlingUnit1);
        orderAirHandlingUnitRepository.save(orderAirHandlingUnit2);

        orderAirConditioningDeviceRepository.save(orderAirConditioningDevice1);
        orderAirConditioningDeviceRepository.save(orderAirConditioningDevice2);
        orderAirConditioningDeviceRepository.save(orderAirConditioningDevice3);

        ordersActionHistoryRepository.save(ordersActionHistory1);
        ordersActionHistoryRepository.save(ordersActionHistory2);
        ordersActionHistoryRepository.save(ordersActionHistory3);
        ordersActionHistoryRepository.save(ordersActionHistory4);
        ordersActionHistoryRepository.save(ordersActionHistory5);
        ordersActionHistoryRepository.save(ordersActionHistory6);
        ordersActionHistoryRepository.save(ordersActionHistory7);

        orderMiningFarmRepository.save(orderMiningFarm1);
        orderMiningFarmRepository.save(orderMiningFarm2);
        orderMiningFarmRepository.save(orderMiningFarm3);

        Order foundOrder1 = orderRepository.findById(1L).get();
        Order foundOrder2 = orderRepository.findById(2L).get();
        Order foundOrder3 = orderRepository.findById(3L).get();
        Order foundOrder4 = orderRepository.findById(4L).get();

        foundOrder1.setName(foundOrder1.getOrderType() + " " + foundOrder1.getOrderId());
        foundOrder2.setName(foundOrder2.getOrderType() + " " + foundOrder2.getOrderId());
        foundOrder3.setName(foundOrder3.getOrderType() + " " + foundOrder3.getOrderId());
        foundOrder4.setName(foundOrder4.getOrderType() + " " + foundOrder4.getOrderId());

        orderRepository.save(foundOrder1);
        orderRepository.save(foundOrder2);
        orderRepository.save(foundOrder3);
        orderRepository.save(foundOrder4);
    }
}
