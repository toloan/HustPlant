package com.example.bach0.hustplant.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.bach0.hustplant.App;
import com.example.bach0.hustplant.database.entity.Plant;

/**
 * Created by bach0 on 4/15/2018.
 */

public class Initializer extends Callback {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PlantDao plantDao = App.get().getDatabase().plantDao();
                Plant[] plants = {
                        new Plant("Oak", "Oaks have spirally arranged leaves, with lobate margins" +
                                " in many species; some have serrated leaves or entire leaves " +
                                "with smooth margins. Many deciduous species are marcescent, not " +
                                "dropping dead leaves until spring. In spring, a single oak tree " +
                                "produces both male flowers (in the form of catkins) and small " +
                                "female flowers. The fruit is a nut called an acorn or oak nut" +
                                " borne in a cup-like structure known as a cupule; each acorn " +
                                "contains one seed (rarely two or three) and takes 6–18 months to" +
                                " mature, depending on their species. The acorns and leaves " +
                                "contain tannic acid, which helps to guard from fungi and " +
                                "insects. The live oaks are distinguished for being evergreen," +
                                " but are not actually a distinct group and instead are dispersed" +
                                " across the genus."),
                        new Plant("Maple", "Maples are distinguished by opposite leaf arrangement" +
                                ". The leaves in most species are palmate veined and lobed, with " +
                                "3 to 9 (rarely to 13) veins each leading to a lobe, one of which" +
                                " is central or apical. A small number of species differ in " +
                                "having palmate compound, pinnate compound, pinnate veined or " +
                                "unlobed leaves."),
                        new Plant("Cherry blossom", "The most popular variety of cherry blossom " +
                                "in Japan is the Somei Yoshino. Its flowers are nearly pure " +
                                "white, tinged with the palest pink, especially near the stem. " +
                                "They bloom and usually fall within a week, before the leaves " +
                                "come out. Therefore, the trees look nearly white from top to " +
                                "bottom. The variety takes its name from the village of Somei " +
                                "(now part of Toshima in Tokyo). It was developed in the mid- to " +
                                "late-19th century at the end of the Edo period and the beginning" +
                                " of the Meiji period. The Somei Yoshino is so widely associated " +
                                "with cherry blossoms that jidaigeki and other works of fiction " +
                                "often depict the variety in the Edo period or earlier; such " +
                                "depictions are anachronisms."),
                        new Plant("Hazel", "Hazels have simple, rounded leaves with " +
                                "double-serrate margins. The flowers are produced very early in " +
                                "spring before the leaves, and are monoecious, with single-sex " +
                                "catkins, the male catkins are pale yellow and 5–12 cm long, and " +
                                "the female ones are very small and largely concealed in the " +
                                "buds, with only the bright-red, 1-to-3 mm-long styles visible. " +
                                "The fruits are nuts 1–2.5 cm long and 1–2 cm diameter, " +
                                "surrounded by an involucre (husk) which partly to fully encloses" +
                                " the nut.")
                };
                plantDao.insertAll(plants);
            }
        });
    }
}
