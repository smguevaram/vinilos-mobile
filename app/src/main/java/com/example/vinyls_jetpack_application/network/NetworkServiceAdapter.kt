package com.example.vinyls_jetpack_application.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.models.Comment
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://vynils-back-heroku.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun getAlbums(onComplete:(resp:List<Album>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i,
                        Album(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            cover = item.getString("cover"),
                            recordLabel = item.getString("recordLabel"),
                            releaseDate = item.getString("releaseDate"),
                            genre = item.getString("genre"),
                            description = item.getString("description"),)
                    )
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getAlbum(albumId: Int, onComplete: (resp: Album) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val item = JSONObject(response)
                val album = Album(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    cover = item.getString("cover"),
                    recordLabel = item.getString("recordLabel"),
                    releaseDate = item.getString("releaseDate"),
                    genre = item.getString("genre"),
                    description = item.getString("description"),
                )
                onComplete(album)
            },
            {
                onError(it)
            }))
    }

    fun addAlbum(body: JSONObject, onComplete:(resp:Album)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums",
            body,
            { response ->
                val album = Album(
                    id = response.getInt("id"),
                    name = response.getString("name"),
                    cover = response.getString("cover"),
                    recordLabel = response.getString("recordLabel"),
                    releaseDate = response.getString("releaseDate"),
                    genre = response.getString("genre"),
                    description = response.getString("description"),
                )
                onComplete(album)
            },
            {
                Log.d("Error", it.message.toString())
                onError(it)
            }))
    }


    fun getArtists(onComplete:(resp:List<Artist>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("musicians",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Artist>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i,
                        Artist(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            image = item.getString("image"),
                            description = item.getString("description"),
                            birthDate = item.getString("birthDate"),
                        )
                    )
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getArtist(id:Int, onComplete:(resp:Artist)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("musicians/$id",
            { response ->
                val item = JSONObject(response)
                val artist = Artist(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    image = item.getString("image"),
                    description = item.getString("description"),
                    birthDate = item.getString("birthDate"),
                )
                onComplete(artist)
            },
            {
                onError(it)
            }))
    }

    fun getCollectors(onComplete:(resp:List<Collector>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(id = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")))
                }
                onComplete(list)
            },
            {
                onError(it)
                Log.d("", it.message.toString())
            }))
    }

    fun getCollector(id:Int, onComplete:(resp:Collector)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("collectors/$id",
            { response ->
                val item = JSONObject(response)
                val collector = Collector(id = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email"))
                onComplete(collector)
            },
            {
                onError(it)
            }))
    }


    fun getComments(albumId:Int, onComplete:(resp:List<Comment>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("albums/$albumId/comments",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Comment>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    Log.d("Response", item.toString())
                    list.add(i, Comment(id = item.getInt("id"), rating = item.getInt("rating"), description = item.getString("description"), collector = "test"))
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }
    fun postComment(body: JSONObject, albumId: Int,  onComplete:(resp:Comment)->Unit , onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums/$albumId/comments",
            body,
            { response ->
                val comment = JSONObject(response.toString())
                val commentToReturn = Comment(id = comment.getInt("id"), rating = comment.getInt("rating"), description = comment.getString("description"), collector = "test")
                onComplete(commentToReturn)
            },
            {
                onError(it)
            }))
    }
    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}