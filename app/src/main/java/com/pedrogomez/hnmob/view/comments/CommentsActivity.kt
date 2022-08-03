package com.pedrogomez.hnmob.view.comments

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedrogomez.hnmob.databinding.ActivityCommentsBinding
import com.pedrogomez.hnmob.view.comments.viewmodel.AuthViewModel
import com.pedrogomez.hnmob.view.comments.viewmodel.CommentsViewModel
import com.pedrogomez.hnmob.view.login.SignInActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private lateinit var manager: LinearLayoutManager

    private lateinit var adapter: FriendlyMessageAdapter

    private val commentsViewModel : CommentsViewModel by viewModel()
    private val authViewModel : AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This codelab uses View Binding
        // See: https://developer.android.com/topic/libraries/view-binding
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.checkLogin()

        // Initialize Firebase Auth and check if the user is signed in
        authViewModel.isAutenticated.observe(this){
            if (!it) {
                startActivity(
                    Intent(
                        this,
                        SignInActivity::class.java
                    )
                )
                finish()
            }
        }

        intent?.extras.let { bundle ->
            //put string with no comments alert
            if(bundle!=null){
                bundle.getString(IDPOST)?.let { idPost ->
                    adapter = FriendlyMessageAdapter(
                        commentsViewModel.getComments(idPost),
                        authViewModel.getUserName()
                    )
                }
            }
        }

        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver for details
        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        )

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver for details
        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))

        // When the send button is clicked, send a text message
        binding.sendButton.setOnClickListener {
            commentsViewModel.publishComment(
                binding.messageEditText.text.toString(),
                authViewModel.getUserName()
            )
            binding.messageEditText.setText("")
        }

    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    companion object {
        private const val TAG = "MainActivity"
        const val COMMENTS = "comments"
        const val ANONYMOUS = "anonymous"
        const val IDPOST = "idPost"
    }
}
