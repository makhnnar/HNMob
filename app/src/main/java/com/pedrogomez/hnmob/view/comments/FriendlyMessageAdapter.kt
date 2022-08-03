package com.pedrogomez.hnmob.view.comments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.MessageBinding
import com.pedrogomez.hnmob.view.comments.CommentsActivity.Companion.ANONYMOUS
import com.pedrogomez.hnmob.view.comments.model.FriendlyMessage

// The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
// See: https://github.com/firebase/FirebaseUI-Android
class FriendlyMessageAdapter(
    private val options: FirebaseRecyclerOptions<FriendlyMessage>,
    private val currentUserName: String?
) :
    FirebaseRecyclerAdapter<FriendlyMessage, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message, parent, false)
        val binding = MessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: FriendlyMessage) {
        if (options.snapshots[position].text != null) {
            (holder as MessageViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    inner class MessageViewHolder(private val binding: MessageBinding) : ViewHolder(binding.root) {
        fun bind(item: FriendlyMessage) {
            binding.messageTextView.text = item.text
            setTextColor(item.name, binding.messageTextView)
            binding.messengerTextView.text = if (item.name == null) ANONYMOUS else item.name
        }

        private fun setTextColor(userName: String?, textView: TextView) {
            if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_gray)
                textView.setTextColor(Color.BLACK)
            }
        }
    }

    companion object {
        const val TAG = "MessageAdapter"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
    }
}
