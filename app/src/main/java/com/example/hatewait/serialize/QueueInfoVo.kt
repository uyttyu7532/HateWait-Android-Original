package com.example.hatewait.serialize

import java.io.Serializable

class QueueInfoVo : Serializable {
    var id: String? = null
    var phone = 0
    var name: String? = null
    var peopleNum = 0
    var turn = 0
    override fun toString(): String {
        return ("QueueInfoVo [id=" + id + ", phone=" + phone + ", name=" + name + ", peopleNum=" + peopleNum + ", turn="
                + turn + "]")
    }

}