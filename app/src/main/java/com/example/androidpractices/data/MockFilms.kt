package com.example.androidpractices.data

import com.example.androidpractices.model.Film

/** Моковые данные. В практике 4 их заменит ответ сервера. */
object MockFilms {
    val films: List<Film> = listOf(
        Film(
            id = "castle-in-the-sky",
            title = "Небесный замок Лапута",
            originalTitle = "天空の城ラピュタ",
            originalTitleRomanised = "Tenkū no Shiro Rapyuta",
            description = "Девочка Сита и мальчик Пазу отправляются на поиски легендарного " +
                "летающего острова Лапута, за которым охотятся военные и воздушные пираты.",
            director = "Hayao Miyazaki",
            producer = "Isao Takahata",
            releaseYear = 1986,
            runningTimeMinutes = 124,
            rtScore = 95,
        ),
        Film(
            id = "grave-of-the-fireflies",
            title = "Могила светлячков",
            originalTitle = "火垂るの墓",
            originalTitleRomanised = "Hotaru no Haka",
            description = "История брата и сестры, которые пытаются выжить в Японии в последние " +
                "месяцы Второй мировой войны после гибели дома и семьи.",
            director = "Isao Takahata",
            producer = "Toru Hara",
            releaseYear = 1988,
            runningTimeMinutes = 89,
            rtScore = 97,
        ),
        Film(
            id = "my-neighbor-totoro",
            title = "Мой сосед Тоторо",
            originalTitle = "となりのトトロ",
            originalTitleRomanised = "Tonari no Totoro",
            description = "Две сестры переезжают с отцом в деревню и знакомятся с добрыми " +
                "лесными духами, главный из которых — огромный пушистый Тоторо.",
            director = "Hayao Miyazaki",
            producer = "Hayao Miyazaki",
            releaseYear = 1988,
            runningTimeMinutes = 86,
            rtScore = 93,
        ),
        Film(
            id = "kikis-delivery-service",
            title = "Служба доставки Кики",
            originalTitle = "魔女の宅急便",
            originalTitleRomanised = "Majo no Takkyūbin",
            description = "Юная ведьма Кики по традиции уезжает на год из дома, поселяется " +
                "в приморском городе и открывает службу воздушной доставки.",
            director = "Hayao Miyazaki",
            producer = "Hayao Miyazaki",
            releaseYear = 1989,
            runningTimeMinutes = 102,
            rtScore = 96,
        ),
        Film(
            id = "only-yesterday",
            title = "Только вчера",
            originalTitle = "おもひでぽろぽろ",
            originalTitleRomanised = "Omoide Poro Poro",
            description = "Офисная работница Таэко едет в деревню собирать цветы сафлора и " +
                "вспоминает своё детство, пытаясь понять, чего она хочет сейчас.",
            director = "Isao Takahata",
            producer = "Toshio Suzuki",
            releaseYear = 1991,
            runningTimeMinutes = 118,
            rtScore = 100,
        ),
        Film(
            id = "porco-rosso",
            title = "Порко Россо",
            originalTitle = "紅の豚",
            originalTitleRomanised = "Kurenai no Buta",
            description = "Итальянский лётчик, превратившийся в свинью, охотится за воздушными " +
                "пиратами над Адриатическим морем между двумя мировыми войнами.",
            director = "Hayao Miyazaki",
            producer = "Toshio Suzuki",
            releaseYear = 1992,
            runningTimeMinutes = 93,
            rtScore = 94,
        ),
        Film(
            id = "princess-mononoke",
            title = "Принцесса Мононоке",
            originalTitle = "もののけ姫",
            originalTitleRomanised = "Mononoke Hime",
            description = "Юноша Аситака, получивший проклятие демона, отправляется на запад " +
                "и оказывается между людьми железного города и духами леса.",
            director = "Hayao Miyazaki",
            producer = "Toshio Suzuki",
            releaseYear = 1997,
            runningTimeMinutes = 134,
            rtScore = 92,
        ),
        Film(
            id = "spirited-away",
            title = "Унесённые призраками",
            originalTitle = "千と千尋の神隠し",
            originalTitleRomanised = "Sen to Chihiro no Kamikakushi",
            description = "Десятилетняя Тихиро попадает в мир духов и должна работать в бане " +
                "для богов, чтобы спасти родителей, превращённых в свиней.",
            director = "Hayao Miyazaki",
            producer = "Toshio Suzuki",
            releaseYear = 2001,
            runningTimeMinutes = 125,
            rtScore = 97,
        ),
        Film(
            id = "howls-moving-castle",
            title = "Ходячий замок",
            originalTitle = "ハウルの動く城",
            originalTitleRomanised = "Hauru no Ugoku Shiro",
            description = "Шляпница София, превращённая проклятием в старуху, находит приют " +
                "в ходячем замке волшебника Хаула.",
            director = "Hayao Miyazaki",
            producer = "Toshio Suzuki",
            releaseYear = 2004,
            runningTimeMinutes = 119,
            rtScore = 87,
        ),
        Film(
            id = "ponyo",
            title = "Рыбка Поньо на утёсе",
            originalTitle = "崖の上のポニョ",
            originalTitleRomanised = "Gake no Ue no Ponyo",
            description = "Золотая рыбка Поньо мечтает стать человеком после встречи с " +
                "пятилетним мальчиком Сосукэ, и её желание нарушает равновесие океана.",
            director = "Hayao Miyazaki",
            producer = "Toshio Suzuki",
            releaseYear = 2008,
            runningTimeMinutes = 103,
            rtScore = 92,
        ),
    )
}
