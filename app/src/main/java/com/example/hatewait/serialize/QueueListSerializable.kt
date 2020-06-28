package com.example.hatewait.serialize

import java.io.Serializable

class QueueListSerializable : Serializable {
    var autonum = 0
    var qivo: List<QueueInfoVo>? = null
    override fun toString(): String {
        return "QueueListSerializable [ INFO=" + info + ", autonum=" + autonum + ", qivo=" + qivo + "]"
    }

    companion object {
        private const val serialVersionUID = 1L
        const val info = "STRQUE"
    }
}