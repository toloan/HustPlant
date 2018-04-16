package com.example.bach0.hustplant.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.graphics.Point;
import android.support.annotation.NonNull;

import com.example.bach0.hustplant.App;
import com.example.bach0.hustplant.R;
import com.example.bach0.hustplant.database.entity.Person;
import com.example.bach0.hustplant.database.entity.Plant;
import com.example.bach0.hustplant.database.entity.Water;
import com.example.bach0.hustplant.database.entity.WaterHistory;

import java.util.Date;
import java.util.concurrent.Executors;

/** Created by bach0 on 4/15/2018. */
public class Initializer extends Callback {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        Executors.newSingleThreadScheduledExecutor()
                .execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                Plant[] plants = {
                                    new Plant(
                                            1,
                                            new Point(92, 95),
                                            "Oak",
                                            "Oaks have spirally arranged leaves, with lobate margins in many species; some have serrated leaves or entire leaves with smooth margins. Many deciduous species are marcescent, not dropping dead leaves until spring. In spring, a single oak tree produces both male flowers (in the form of catkins) and small female flowers. The fruit is a nut called an acorn or oak nut borne in a cup-like structure known as a cupule; each acorn contains one seed (rarely two or three) and takes 6–18 months to mature, depending on their species. The acorns and leaves contain tannic acid, which helps to guard from fungi and insects. The live oaks are distinguished for being evergreen, but are not actually a distinct group and instead are dispersed across the genus.",
                                            0.5f,
                                            0.7f,
                                            R.drawable.ic_menu_camera),
                                    new Plant(
                                            2,
                                            new Point(137, 155),
                                            "Maple",
                                            "Maples are distinguished by opposite leaf arrangement. The leaves in most species are palmate veined and lobed, with 3 to 9 (rarely to 13) veins each leading to a lobe, one of which is central or apical. A small number of species differ in having palmate compound, pinnate compound, pinnate veined or unlobed leaves.",
                                            0.2f,
                                            0.8f,
                                            R.drawable.ic_menu_gallery),
                                    new Plant(
                                            3,
                                            new Point(227, 120),
                                            "Cherry Blossom",
                                            "The most popular variety of cherry blossom in Japan is the Somei Yoshino. Its flowers are nearly pure white, tinged with the palest pink, especially near the stem. They bloom and usually fall within a week, before the leaves come out. Therefore, the trees look nearly white from top to bottom. The variety takes its name from the village of Somei (now part of Toshima in Tokyo). It was developed in the mid- to late-19th century at the end of the Edo period and the beginning of the Meiji period. The Somei Yoshino is so widely associated with cherry blossoms that jidaigeki and other works of fiction often depict the variety in the Edo period or earlier; such depictions are anachronisms.",
                                            0.4f,
                                            0.6f,
                                            R.drawable.ic_menu_manage),
                                    new Plant(
                                            4,
                                            new Point(262, 74),
                                            "Hazel",
                                            "Hazels have simple, rounded leaves with double-serrate margins. The flowers are produced very early in spring before the leaves, and are monoecious, with single-sex catkins, the male catkins are pale yellow and 5–12 cm long, and the female ones are very small and largely concealed in the buds, with only the bright-red, 1-to-3 mm-long styles visible. The fruits are nuts 1–2.5 cm long and 1–2 cm diameter, surrounded by an involucre (husk) which partly to fully encloses the nut.",
                                            0.15f,
                                            0.25f,
                                            R.drawable.ic_menu_send)
                                };
                                PlantDao plantDao = App.get().getDatabase().plantDao();
                                plantDao.insertAll(plants);
                                Person[] persons = {
                                    new Person(1, "Dang Xuan Bach"),
                                    new Person(2, "Hoang To Loan"),
                                    new Person(3, "Pham Thi Hanh Tuyen"),
                                    new Person(4, "Nguyen Dinh Tho"),
                                    new Person(5, "Son")
                                };
                                PersonDao personDao = App.get().getDatabase().personDao();
                                personDao.insertAll(persons);
                                WaterHistory[] waterHistories = {
                                    new WaterHistory(1, 1, 1, 0.5f, new Date(1523711333)),
                                    new WaterHistory(2, 2, 4, 0.2f, new Date(1523731331)),
                                    new WaterHistory(3, 3, 1, 0.3f, new Date(1523761376)),
                                    new WaterHistory(4, 1, 2, 0.6f, new Date(1523762342)),
                                    new WaterHistory(5, 4, 1, 0.1f, new Date(1523765337)),
                                    new WaterHistory(6, 5, 3, 0.24f, new Date(1523661331)),
                                    new WaterHistory(7, 2, 1, 0.41f, new Date(1523711338)),
                                    new WaterHistory(8, 3, 1, 0.63f, new Date(1523461328)),
                                    new WaterHistory(9, 1, 4, 0.12f, new Date(1523721139)),
                                    new WaterHistory(10, 1, 1, 0.6f, new Date(1523661350)),
                                    new WaterHistory(11, 1, 2, 0.2f, new Date(1523161380)),
                                };
                                WaterHistoryDao waterHistoryDao =
                                        App.get().getDatabase().waterHistoryDao();
                                waterHistoryDao.insertAll(waterHistories);
                                Water[] waters = {
                                    new Water(1, new Point(114, 345)),
                                    new Water(2, new Point(229, 466)),
                                };
                                WaterDao waterDao = App.get().getDatabase().waterDao();
                                waterDao.insertAll(waters);
                            }
                        });
    }
}
