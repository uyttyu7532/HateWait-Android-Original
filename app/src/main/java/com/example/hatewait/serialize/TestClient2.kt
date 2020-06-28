package com.example.hatewait.serialize

import java.io.*
import java.net.Socket

object TestClient2 {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val sock = Socket("127.0.0.1", 3000)
            val keyboard =
                BufferedReader(InputStreamReader(System.`in`))
            val out = sock.getOutputStream()
            val ois =
                ObjectInputStream(sock.getInputStream()) //객체 받는 스트림
            val pw = PrintWriter(OutputStreamWriter(out))
            var line: String? = null //단순 명령어입력.. 보내실 땐 그냥 프로토콜에 있는 대로 문자열로 보내시면 됩니당
            while (keyboard.readLine().also { line = it } != null) {
                //명령어 보내는 파트(필요없음)
                pw.println(line)
                pw.flush()

                //객체 받는 파트(QueueListSerializable 객체로 받아와요! 객체 코드 참조!
                val qls = ois.readObject() as QueueListSerializable

                //이건 그냥 잘 받아왔는지 출력하는 코드.
                println("서버한테서 받은거::::: $qls")
            }
            pw.close()
            ois.close()
            sock.close()
        } catch (e: Exception) {
            println(e)
        }
    }
}