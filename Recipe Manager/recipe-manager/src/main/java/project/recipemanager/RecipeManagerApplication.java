package project.recipemanager;

import com.sun.istack.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.recipemanager.model.*;
import project.recipemanager.repository.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;


@SpringBootApplication
public class RecipeManagerApplication {

    private Logger log = Logger.getLogger(RecipeManagerApplication.class);

    @Autowired
    private AuthorityRepository authRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public static void main(String[] args) {

        SpringApplication.run(RecipeManagerApplication.class, args);
    }

    @PostConstruct
    public void initializeDb() {

        //Utworzenie rodzjów kont w aplikacji i dodanie ich do bazy
        Authority adminAuth = new Authority("ROLE_ADMIN");
        Authority userAuth = new Authority("ROLE_USER");

        log.info("Authority " + authRepo.save(adminAuth).getAuthority() + " added.");
        log.info("Authority " + authRepo.save(userAuth).getAuthority() + " added.");


        //Utworzenie konta administratora
        List<Authority> adminAuthorities = new ArrayList<>();
        adminAuthorities.add(authRepo.findByAuthority("ROLE_USER"));
        adminAuthorities.add(authRepo.findByAuthority("ROLE_ADMIN"));
        User admin = new User("admin", bCryptPasswordEncoder.encode("admin"), adminAuthorities);
        admin.setEnabled(true);
        admin.setName("Administrator");
        admin.setSurname("Systemu");
        log.config("User " + userRepository.save(admin).getUsername() + " added.");

        //Utworzenie konta użytkownika
        List<Authority> userAuthorities = new ArrayList<>();
        userAuthorities.add(authRepo.findByAuthority("ROLE_USER"));
        User user1 = new User("user1", bCryptPasswordEncoder.encode("user1"), userAuthorities);
        user1.setEnabled(true);
        user1.setName("Jan");
        user1.setSurname("Kowalski");
        user1.setEmail("kowal1990@gmail.com");
        log.config("User " + userRepository.save(user1).getUsername() + " added.");

        User user2 = new User("user2", bCryptPasswordEncoder.encode("user2"), userAuthorities);
        user2.setEnabled(true);
        user2.setName("Piotr");
        user2.setSurname("Nowak");
        user2.setEmail("piotr.nowak@gmail.com");
        log.config("User " + userRepository.save(user2).getUsername() + " added.");

        User user3 = new User("user3", bCryptPasswordEncoder.encode("user3"), userAuthorities);
        user3.setEnabled(false);
        user3.setName("Mariusz");
        user3.setSurname("Kowalski");
        user3.setEmail("mariush-kowalsky@wp.pl");
        log.config("User " + userRepository.save(user3).getUsername() + " added.");

        User user4 = new User("user4", bCryptPasswordEncoder.encode("user4"), userAuthorities);
        user4.setEnabled(true);
        user4.setBanned(true);
        user4.setName("Paweł");
        user4.setSurname("Wiśniowski");
        user4.setEmail("wisnia88@onet.pl");
        log.config("User " + userRepository.save(user4).getUsername() + " added.");


        Recipe recipe1 = new Recipe("Chleb", "Krok 1\n" +
                "Ziarna chia namocz w 125ml ciepłej i przegotowanej wody na 30 minut. Rozgrzej piekarnik do 160 stopni. Orzechy włoskie posiekaj.\n" +
                "\n" +
                "Krok 2\n" +
                "W dużej misce wymieszaj najpierw mąkę gryczaną z proszkiem do pieczenia, solą, przyprawą Knorr, płatkami owsianymi, orzechami i słonecznikiem.\n" +
                "\n" +
                "Krok 3\n" +
                "Dodaj twaróg oraz namoczone ziarna chia wraz z zalewą i wymieszaj ciasto.\n" +
                "\n" +
                "Krok 4\n" +
                "Posmaruj foremkę oliwą i wysyp płatkami owsianymi. Następnie włóż ciasto. Posyp ziarnami sezamu.\n" +
                "\n" +
                "Krok 5\n" +
                "Piecz przez ok 55-60 minut. Po upieczeniu wyjmij z foremki, połóż chleb na kratce z piekarnika i pozwól aby powoli ostygł.", user1);
        Ingredient i1_1 = new Ingredient("nasiona chia", "2 łyżki", recipe1);
        Ingredient i1_2 = new Ingredient("mąka gryczana", "50 gramów", recipe1);
        Ingredient i1_3 = new Ingredient("płatki owsiane", "100 gramów", recipe1);
        Ingredient i1_4 = new Ingredient("proszek do pieczenia", "1 opakowanie", recipe1);
        Ingredient i1_5 = new Ingredient("orzechy włoskie", "50 gramów", recipe1);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(i1_1);
        ingredients.add(i1_2);
        ingredients.add(i1_3);
        ingredients.add(i1_4);
        ingredients.add(i1_5);
        recipe1.setIngredients(ingredients);
        ingredientRepository.save(i1_1);
        ingredientRepository.save(i1_2);
        ingredientRepository.save(i1_3);
        ingredientRepository.save(i1_4);
        ingredientRepository.save(i1_5);
        recipeRepository.save(recipe1);

        Recipe recipe2 = new Recipe("Sernik", "Krok 1\n" +
                "Miękkie masło ubijamy z cukrem pudrem, aż powstanie puszysta masa. Po chwili dodajemy po jednym żółtku cały czas mieszając.\n" +
                "\n" +
                "Sernik - Krok 1\n" +
                "Krok 2\n" +
                "Następnie dodajemy twaróg sernikowy i nadal mieszamy.\n" +
                "\n" +
                "Sernik - Krok 2\n" +
                "Krok 3\n" +
                "Do powstałej masy dodajemy cukier waniliowy, mąkę ziemniaczaną oraz śmietankę. Cały czas mieszamy, aż do połączenia się składników.\n" +
                "\n" +
                "Sernik - Krok 3\n" +
                "Krok 4\n" +
                "Białka ubijamy na sztywną pianę. Dodajemy do masy serowej. Delikatnie mieszamy, nie za długo, aby masa się nie napowietrzyła.\n" +
                "\n" +
                "Sernik - Krok 4\n" +
                "Krok 5\n" +
                "Formę na sernik wykładamy papierem do pieczenia. Powstałą masę przelewamy do formy i wyrównujemy wierzch. Pieczemy ok 55 min w temperaturze ok 170 C. Studzimy przy uchylonych drzwiczkach. Przed podaniem posypujemy cukrem pudrem.", user1);
        Ingredient i2_1 = new Ingredient("twaróg sernikowy", "800 gramów", recipe2);
        Ingredient i2_2 = new Ingredient("cukier puder", "200 gramów", recipe2);
        Ingredient i2_3 = new Ingredient("masło", "200 gramów", recipe2);
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(i2_1);
        ingredients2.add(i2_2);
        ingredients2.add(i2_3);
        recipe2.setIngredients(ingredients2);
        ingredientRepository.save(i2_1);
        ingredientRepository.save(i2_2);
        ingredientRepository.save(i2_3);
        recipeRepository.save(recipe2);

        Recipe recipe3 = new Recipe("Gofry bez cukru", "Krok 1\n" +
                "Cytrynę umyj, spaż wrzątkiem otrzyj z niej skórkę, a następnie wyciśnij sok. Laskę wanilii rozetnij na pół po długości, wyskrob z niej pestki, goździki rozetrzyj w moździerzu na proch.\n" +
                "\n" +
                "Gofry bez cukru - Krok 1\n" +
                "Krok 2\n" +
                "Fasolę odcedź, ale niedokładnie, umieść w kielichu miksera, dodaj orzechy i daktyle, sok z cytryny i zmiksuj dokładnie.\n" +
                "\n" +
                "Gofry bez cukru - Krok 2\n" +
                "Krok 3\n" +
                "Daktyle z orzechami i fasolą przełóż do miski, dodaj mąkę, olej, jogurt, sodę, sól, pestki wanilii i goździki, skórkę z cytryny i dokładnie wymieszaj wraz z jajkami. Ciasto powinno mieć konsystencję gęstą, ale lejąca się, w razie potrzeby wyreguluj ją dodając odrobinę wody.\n" +
                "\n" +
                "Gofry bez cukru - Krok 3\n" +
                "Krok 4\n" +
                "Rozgrzej maszynę do gofrów, posmaruj ją olejem, nałóż odpowiednią ilość ciasta i piecz aż ciasto się zarumieni i łatwo będzie odchodzić od gofrownicy.", user1);
        Ingredient i3_1 = new Ingredient("mąka z amarantusa", "150 gramów", recipe3);
        Ingredient i3_2 = new Ingredient("jajka", "2 sztuki", recipe3);
        Ingredient i3_3 = new Ingredient("ugotowana biała fasola", "1 puszka", recipe3);
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(i3_1);
        ingredients3.add(i3_2);
        ingredients3.add(i3_3);
        recipe3.setIngredients(ingredients3);
        ingredientRepository.save(i3_1);
        ingredientRepository.save(i3_2);
        ingredientRepository.save(i3_3);
        recipeRepository.save(recipe3);

        Recipe recipe4 = new Recipe("Spaghetti bolognese", "Krok 1\n" +
                "Mięso mielone podsmaż przez około 8 minut na gorącym oleju.\n" +
                "\n" +
                "Spaghetti bolognese  - Krok 1\n" +
                "Krok 2\n" +
                "Fix wymieszaj z 250 mililitrami zimnej wody, dodaj do smażonego mięsa. Całość doprowadź do wrzenia. Mięso duś jeszcze pod przykryciem około 10 minut, mieszając sos co jakiś czas.\n" +
                "\n" +
                "Spaghetti bolognese  - Krok 2\n" +
                "Krok 3\n" +
                "Na koniec dodaj dwa pokrojone w kostkę pomidory i duś jeszcze wszystko razem około 3 minut.\n" +
                "\n" +
                "Spaghetti bolognese  - Krok 3\n" +
                "Krok 4\n" +
                "Sos podawaj wymieszany z ugotowanym na sposób al dente makaronem i tartym serem.", user1);
        Ingredient i4_1 = new Ingredient("mięso mielone wołowo-wieprzowe", "300 gramów", recipe4);
        Ingredient i4_2 = new Ingredient("makaron", "300 gramów", recipe4);
        Ingredient i4_3 = new Ingredient("Sos do Spaghetti Bolognese Knorr", "1 opakowanie", recipe4);
        List<Ingredient> ingredients4 = new ArrayList<>();
        ingredients4.add(i4_1);
        ingredients4.add(i4_2);
        ingredients4.add(i4_3);
        recipe4.setIngredients(ingredients4);
        ingredientRepository.save(i4_1);
        ingredientRepository.save(i4_2);
        ingredientRepository.save(i4_3);
        recipeRepository.save(recipe4);


        //Utworzenie obiektu przepisu- podaje tytuł opis i autora
        Recipe recipe5 = new Recipe("Lody", "Krok 1\n" +
                "Zetnij wypukły grzbiet banana, obierz część skórki i ułóż na środku talerza (tak jak na zdjęciu).\n" +
                "\n" +
                "Krok 2\n" +
                "3 małe kulki lodów, każdą o innym smaku, ułóż na obranej części banana.\n" +
                "\n" +
                "Krok 3\n" +
                "Rozgrzaną płynną polewą namaluj na każdej kulce oczy i uśmiechniętą buzię.\n" +
                "\n" +
                "Krok 4\n" +
                "Z posypki zrób kolorowe czuprynki wszystkim pasażerom kanu.\n" +
                "\n" +
                "Krok 5\n" +
                "Na końcach banana przytwierdź lukrowane kwiatki.\n" +
                "\n" +
                "Krok 6\n" +
                "Deser dla dzieci udekoruj posypką, drażetkami i czekoladowymi cukierkami.", user1);


        //utworzenie obiektów składników-podaje nazwe oraz ilość oraz przepis którego dotyczą
        Ingredient i5_1 = new Ingredient("banany", "2 sztuki", recipe5);
        Ingredient i5_2 = new Ingredient("polewa", "", recipe5);
        Ingredient i5_3 = new Ingredient("big milk tropicana", "1 opakowanie", recipe5);
        Ingredient i5_4 = new Ingredient("posypka", "1 opakowanie", recipe5);

       //zgrupowanie składników w liste
        List<Ingredient> ingredients5 = new ArrayList<>();

        //dodawanie składników do listy
        ingredients5.add(i5_1);
        ingredients5.add(i5_2);
        ingredients5.add(i5_3);
        ingredients5.add(i5_4);

       //ustawienie przepisowi listy składników
        recipe5.setIngredients(ingredients5);

        //zapisanie obiektów składników do bazy danych
        ingredientRepository.save(i5_1);
        ingredientRepository.save(i5_2);
        ingredientRepository.save(i5_3);
        ingredientRepository.save(i5_4);

        //zapisanie obiektu do bazy danych
        recipeRepository.save(recipe5);

        Recipe recipe6 = new Recipe("Ciasto czekoladowe", "Przygotuj ciasto i wsadź do pieca", user2);
        recipeRepository.save(recipe6);

        Recipe recipe7 = new Recipe("Ciasteczka czekoladowe", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe7);

        Recipe recipe8 = new Recipe("Barszcz czerwony", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe8);

        Recipe recipe9 = new Recipe("Żurek", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe9);

        Recipe recipe10 = new Recipe("Rosół", "Przygotuj ciasto i wsadź do pieca", user2);
        recipeRepository.save(recipe10);

        Recipe recipe11 = new Recipe("Pomidorówka", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe11);

        Recipe recipe12 = new Recipe("Frytki", "Przygotuj ciasto i wsadź do pieca", user3);
        recipeRepository.save(recipe12);

        Recipe recipe13 = new Recipe("Zupa grzybowa", "Przygotuj ciasto i wsadź do pieca", user4);
        recipeRepository.save(recipe13);

        Recipe recipe14 = new Recipe("Pizza", "Przygotuj ciasto i wsadź do pieca", user3);
        recipeRepository.save(recipe14);

        Recipe recipe15 = new Recipe("Ciasto francuskie", "Przygotuj ciasto i wsadź do pieca", user4);
        recipeRepository.save(recipe15);

        Recipe recipe16 = new Recipe("Sałatka jarzynowa", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe16);

        Recipe recipe17 = new Recipe("Kotlet schabowy", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe17);

        Recipe recipe18 = new Recipe("Kotlet mielony", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe18);

        Recipe recipe19 = new Recipe("Sos pomidorowy", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe19);

        Recipe recipe20 = new Recipe("Kurczak w sosie własnym", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe20);

        Recipe recipe21 = new Recipe("Kopytka", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe21);

        Recipe recipe22 = new Recipe("Naleśniki", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe22);

        Recipe recipe23 = new Recipe("Placki ziemniaczane", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe23);

        Recipe recipe24 = new Recipe("Leczo", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe24);

        Recipe recipe25 = new Recipe("Grochówka", "Przygotuj ciasto i wsadź do pieca", admin);
        recipeRepository.save(recipe25);



        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\chleb.jpg");
            ImageModel img = new ImageModel("obrazek1", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\sernik.jpg");
            ImageModel img = new ImageModel("obrazek2", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\gofry.jpg");
            ImageModel img = new ImageModel("obrazek3", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\spaghetti.jpg");
            ImageModel img = new ImageModel("obrazek4", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\lody.jpg");
            ImageModel img = new ImageModel("obrazek5", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\czeko.jpg");
            ImageModel img = new ImageModel("obrazek6", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\ciasteczka.jpg");
            ImageModel img = new ImageModel("obrazek7", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\barszcz.jpg");
            ImageModel img = new ImageModel("obrazek8", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\żurek.jpg");
            ImageModel img = new ImageModel("obrazek9", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\rosół.jpg");
            ImageModel img = new ImageModel("obrazek10", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\pomidorówka.jpg");
            ImageModel img = new ImageModel("obrazek11", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\frytki.jpg");
            ImageModel img = new ImageModel("obrazek12", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\zupa_grzybowa.jpg");
            ImageModel img = new ImageModel("obrazek13", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\pizza.jpg");
            ImageModel img = new ImageModel("obrazek14", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\ciasto.jpg");
            ImageModel img = new ImageModel("obrazek15", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\salatka.jpg");
            ImageModel img = new ImageModel("obrazek16", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\kopyto.jpg");
            ImageModel img = new ImageModel("obrazek17", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\mielony.jpg");
            ImageModel img = new ImageModel("obrazek18", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\sos.jpg");
            ImageModel img = new ImageModel("obrazek19", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\kurczak.jpg");
            ImageModel img = new ImageModel("obrazek20", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\kopyto.jpg");
            ImageModel img = new ImageModel("obrazek21", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\naleśniki.jpg");
            ImageModel img = new ImageModel("obrazek22", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\placki.jpg");
            ImageModel img = new ImageModel("obrazek23", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\leczo.jpg");
            ImageModel img = new ImageModel("obrazek24", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("C:\\praca inżynierska\\Recipe Manager\\recipe-manager\\src\\main\\resources\\groch.jpg");
            ImageModel img = new ImageModel("obrazek25", "image/jpeg",
                    compressBytes(ImageToByte(myObj)));
            imageRepository.save(img);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    public static byte [] ImageToByte(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }


}
