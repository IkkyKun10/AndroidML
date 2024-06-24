package com.dicoding.asclepius.data.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ResultEntity : RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var resultLabel: String? = null
    var resultScore: String? = null
    var imageUri: String? = null
}