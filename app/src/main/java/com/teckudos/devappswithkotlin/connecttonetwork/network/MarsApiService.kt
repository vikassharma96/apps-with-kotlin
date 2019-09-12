package com.teckudos.devappswithkotlin.connecttonetwork.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

// communicate with server on the internet -
// most webservices today run webservices using a common stateless web architecture known as REST
// REST stands for Representational state transfer. web services offers this architecture called
// Restful Services. Restful services are build using standard web components and protocols. request
// are made to restful webservices in a standardized way by URI.
// URI and URL - URI specify the data we want and URL is a URI
// Each webservices request contains an URI and is transferred to our server using the same HTTP
// protocol that used by web browsers.
// HTTP request contains an operation to tell server what to do eg - operations include
// 1.Get - get data from server 2.Post/Put - put new data in server 3.Delete - delete data from server
// requested data typically in format 1.JSON 2.XML
// Almost any restful service will also accept query parameters as part of the URI. query parameter
// are just like function parameter in kotlin eg - https://mars.udacity.com/realestate?filter=rent
// query parameter separated by location of a service by ? and it contain name value separated by = sign
//              parameters         URI & HTTP Method
//              ----------->        ------------->
// application              Retrofit                  server
//              <----------         <-------------
//               Kotlin Objects         JSON

private const val BASE_URL = "https://mars.udacity.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // json response convert to kotlin object
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) // calladapter adds the ability for retrofit
        // to create api's that return something other than the default call class in this case the
        // coroutine call adapter factory allows us to replace the call and get property with a
        // coroutine differed
    .baseUrl(BASE_URL)
    .build()

// Retrofit creates an object of our interface with all the methods that talk to the server
// conceptually similar room implements our dao
interface MarsApiService {
    @GET("realestate")
//    fun getProperties(): Call<List<MarsProperty>> // call object used to start the request
    fun getProperties() : Deferred<List<MarsProperty>>
    // a deferred is a kind of job coroutine job that can directly return a result
    // as we know job provide to cancel and determine state of coroutine unlike a job differed has
    // a method called await it's a suspend function on deferred it causes code to wait without
    // blocking in true coroutine fashion untill value is ready and then value is returned.
    // retrofit returned differed we await the result which has appearance of synchronous code if
    // there error await will throw an exception
}

// retrofit create call is expensive and our app need only one retrofit service instance will expose
// our retrofit service to the rest of the application using public object called MarsApi
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}

// Retrofit is a library that creates network request for our app, based on the content from our web
// service it fetches data from our web service and routes it through a separate converter library
// that knows how to decode the data and returned it in form of useful objects. retrofit can support
// any data format returned from our web service with the right converter library. We then give
// retrofit an annotated interface to our api it returns an implementation of that interface that we
// can call to talk to our web service then it creates most of network layer for us to do in background.
// Retrofit has at least two things available to it to build our API -
// 1.Base Url of our webservice 2.Converter factory

// Retrofit converters -
// 1.Scalars converter - A Converter which supports converting strings and both primitives and
// their boxed types to text/plain bodies.
// 2.Gson converter - A Converter which uses Gson for serialization to and from JSON.
// Gson - A Java serialization/deserialization library to convert Java Objects into JSON and back.
// 3.Moshi converter - A Converter which uses Moshi for serialization to and from JSON.
// Moshi - modern json library for kotlin and java. parse json to kotlin object