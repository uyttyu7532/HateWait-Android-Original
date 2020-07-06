//package com.example.hatewait
//
//import com.twilio.http.HttpClient
//import com.twilio.http.NetworkHttpClient
//import com.twilio.http.TwilioRestClient
//import org.apache.http.HttpHost
//import org.apache.http.client.config.RequestConfig
//import org.apache.http.impl.client.HttpClientBuilder
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
//
//
//class ProxiedTwilioClientCreator
///**
// * Constructor for ProxiedTwilioClientCreator
// * @param username
// * @param password
// * @param proxyHost
// * @param proxyPort
// */(
//    private val username: String,
//    private val password: String,
//    private val proxyHost: String,
//    private val proxyPort: Int
//) {
//    private var httpClient: HttpClient? = null
//
//    /**
//     * Creates a custom HttpClient based on default config as seen on:
//     * [constructor][com.twilio.http.NetworkHttpClient.NetworkHttpClient]
//     */
//    private fun createHttpClient() {
//        val config = RequestConfig.custom()
//            .setConnectTimeout(10000)
//            .setSocketTimeout(30500)
//            .build()
//        val connectionManager =
//            PoolingHttpClientConnectionManager()
//        connectionManager.defaultMaxPerRoute = 10
//        connectionManager.maxTotal = 10 * 2
//        val proxy = HttpHost(proxyHost, proxyPort)
//        val clientBuilder = HttpClientBuilder.create()
//        clientBuilder
//            .setConnectionManager(connectionManager)
//            .setProxy(proxy)
//            .setDefaultRequestConfig(config)
//
//        // Inclusion of Twilio headers and build() is executed under this constructor
//        httpClient = NetworkHttpClient(clientBuilder)
//    }
//
//    /**
//     * Get the custom client or builds a new one
//     * @return a TwilioRestClient object
//     */
//    val client: TwilioRestClient
//        get() {
//            if (httpClient == null) {
//                createHttpClient()
//            }
//            val builder = TwilioRestClient.Builder(username, password)
//            return builder.httpClient(httpClient).build()
//        }
//
//}