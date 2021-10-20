package com.muslims.firebasemvvm.DummyData

import com.muslims.firebasemvvm.models.User

class DummyDataGenerator {

    fun getDummyUsers() : List<User>{
        return listOf(
            User(id = "1",
                sex = "Male",
                name = "Ahmed",
                age = "23",
                certificate = "High",
                city = "Cairo",
                height = "173",
                isBlocked = false,
                skinColor = "White",
                previousRelation = User.Relation.NO,
                wantChildren = "Yes",
                weight = "62"
                ),
            User(id = "2", sex = "Female"),
        )
    }
}