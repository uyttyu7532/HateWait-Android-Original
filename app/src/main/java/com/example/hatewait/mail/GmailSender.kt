package com.example.hatewait.mail

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

lateinit var emailCode: String

class GmailSender(private val user: String, private val password: String) : Authenticator() {
    private val mailhost = "smtp.gmail.com" // stmp서버 호스트주소
    private val session: Session

    //안드로이드에서 외부로 송출 할때 사용될 인증번호
    //생성된 이메일 인증코드 반환

    private fun createEmailCode(): String {
        //이메일 인증코드 생성
        val str = arrayOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
        )
        var newCode = String()
        for (x in 0..7) {
            val random = (Math.random() * str.size).toInt()
            newCode += str[random]
        }
        return newCode
    }

    override fun getPasswordAuthentication(): PasswordAuthentication {
        //해당 메서드에서 사용자의 계정(id & password)을 받아 인증받으며 인증 실패시 기본값으로 반환됨.
        return PasswordAuthentication(user, password)
    }

    @Synchronized
    @Throws(Exception::class)
    fun sendMail(subject: String?, body: String, recipients: String) {
        val message = MimeMessage(session)
        val handler: DataHandler = DataHandler(
            ByteArrayDataSource(
                body.toByteArray(),
                "text/plain"
            )
        ) //본문 내용을 byte단위로 쪼개어 전달
        message.sender = InternetAddress(user)
        //본인 이메일 설정
        message.subject = subject
        //해당 이메일의 본문 설정
        message.dataHandler = handler
        if (recipients.indexOf(',') > 0) message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(recipients)
        ) else message.setRecipient(Message.RecipientType.TO, InternetAddress(recipients))
        Transport.send(message) //메시지 전달 }
    }

    inner class ByteArrayDataSource : DataSource {
        private var data: ByteArray
        private var type: String? = null

        constructor(data: ByteArray, type: String?) : super() {
            this.data = data
            this.type = type
        }

        constructor(data: ByteArray) : super() {
            this.data = data
        }

        fun setType(type: String?) {
            this.type = type
        }

        override fun getContentType(): String {
            return if (type == null) "application/octet-stream" else type!!
        }

        @Throws(IOException::class)
        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(data)
        }

        override fun getName(): String {
            return "ByteArrayDataSource"
        }

        @Throws(IOException::class)
        override fun getOutputStream(): OutputStream {
            throw IOException("Not Supported")
        }
    }

    init {
        emailCode = createEmailCode()
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", mailhost)
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        props.setProperty("mail.smtp.quitwait", "false")

        //구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달해준다.
        session = Session.getDefaultInstance(props, this)
    }
}