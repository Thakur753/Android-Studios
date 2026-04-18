package com.example.polytechniccofeewala.model

import com.example.polytechniccofeewala.R

data class Coffee(
    val id: Int,
    val name: String,
    val subtitle: String,
    val price: Double,
    val rating: Double,
    val reviews: Int,
    val description: String,
    val image: Int // Resource ID
)

val coffeeList = listOf(
    Coffee(
        1,
        "Caffe Mocha",
        "with Chocolate",
        4.53,
        4.8,
        230,
        "A Caffe Mocha, also called mocaccino, is a chocolate-flavored variant of a caffè latte. Other commonly used spellings are mochaccino and also mochachino. The name is derived from the city of Mocha, Yemen.",
        R.drawable.coffee_mocha
    ),
    Coffee(
        2,
        "Flat White",
        "with Milk",
        3.53,
        4.7,
        150,
        "A flat white is a coffee drink consisting of espresso with microfoam (steamed milk with small, fine bubbles and a glossy or velvety consistency).",
        R.drawable.flat_white
    ),
    Coffee(
        3,
        "Cappuccino",
        "with Cream",
        5.53,
        4.9,
        340,
        "A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with equal parts double espresso, steamed milk, and steamed milk foam on top.",
        R.drawable.cappuccino
    ),
    Coffee(
        4,
        "Americano",
        "with Water",
        3.03,
        4.5,
        120,
        "Caffè Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavor from, traditionally brewed coffee.",
        R.drawable.americano
    ),
    Coffee(
        5,
        "Macheato",
        "with Chocolate",
        4.99,
        4.6,
        180,
        "A caffè macchiato, sometimes called espresso macchiato, is an espresso coffee drink with a small amount of milk, usually foamed.",
        R.drawable.macheato
    ),
    Coffee(
        6,
        "Latte",
        "with Milk",
        4.20,
        4.7,
        210,
        "Caffè latte is a coffee drink of Italian origin made with espresso and steamed milk.",
        R.drawable.latte
    )
)
