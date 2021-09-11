package com.bombadu.techpop.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuItemCompat
import com.bombadu.techpop.R
import com.bombadu.techpop.databinding.ActivityArticleDetailBinding
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.util.Utils
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.net.MalformedURLException
import java.net.URISyntaxException

@AndroidEntryPoint
class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailBinding
    private lateinit var url: String
    private lateinit var title: String
    private lateinit var imageUrl: String
    private val viewModel: DetailViewModel by viewModels()
    private var mMenu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleItem = intent.getParcelableExtra<NewsEntity>("article_key")

        imageUrl = articleItem!!.urlToImage.toString()
        title = articleItem.title.toString()
        url = articleItem.url.toString()
        val date = Utils.convertServerDateToDate(articleItem.publishedAt.toString())
        val author = articleItem.author
        val description = articleItem.description
        val source = articleItem.source



        if (imageUrl != "") {
            Picasso.get().load(imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.missingimage).into(binding.detailImageView)

        } else {
            Picasso.get().load(R.drawable.missingimage)
                .into(binding.detailImageView)
        }

        binding.detailTitleTextView.text = title
        binding.detailAuthorTextView.text = author
        binding.detailDescriptionTextView.text = description
        binding.detailSourceTextView.text = source
        binding.detailDateTextView.text = date

        if (author!!.contains("https")) {
            binding.detailAuthorTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(author)
                startActivity(intent)
            }
        }

        binding.detailFab.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        mMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share -> {
                shareArticle()

            }
            
            R.id.save -> {
                val starAnimation = AnimationUtils.loadAnimation(this, R.anim.star_animation)
                //val star = mMenu?.findItem(R.id.save)?.icon
                val vib: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                val newInsert = SavedEntity(Utils.getTimeStamp(), url, imageUrl, title)
                viewModel.insertSavedArticle(newInsert)
                Toast.makeText(this, "Article Saved", Toast.LENGTH_SHORT).show()
            }
        }
        
        
        return super.onOptionsItemSelected(item)
    }

    private fun shareArticle() {
        val intent: Intent
        val imageUri: Uri
        try {
            val text = StringBuilder().apply {
                append(getString(R.string.share_greeting))
                append(title)
                append("\n")
                append(getString(R.string.share_action1))
                append("\n")
                append(url)
                toString()
            }
            val myDrawable = binding.detailImageView.drawable.toBitmap()
            imageUri = Utils.getImageUri(this, myDrawable)!!
            intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "$text")
            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            intent.type = "image/jpeg"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "send"))

        }catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e1: URISyntaxException) {
            e1.printStackTrace()
        }

    }
}