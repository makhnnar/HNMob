package com.pedrogomez.hnmob.view.hitsslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.FragmentHitsListBinding
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.viewmodel.SharedHitsViewModel
import com.pedrogomez.hnmob.utils.extensions.shortToast
import com.pedrogomez.hnmob.models.result.Result
import com.pedrogomez.hnmob.view.hitsslist.view.HitsListView

class HitsListFragment : Fragment(),
    HitsListView.OnHitsListActions{

    private val sharedHitsViewModel : SharedHitsViewModel by activityViewModels()

    private lateinit var binding: FragmentHitsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentHitsListBinding.inflate(
            inflater,
            container,
            false
        )
        val view = binding.root
        initObservers()
        binding.productsListView.hideBtnToTop()
        binding.productsListView.onHitsListActions = this
        sharedHitsViewModel.loadFirstPage()
        return view
    }

    private fun initObservers(){
        sharedHitsViewModel.hitsListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                binding.productsListView.setData(
                    it
                )
                binding.productsListView.hideLoader()
            }
        )
        sharedHitsViewModel.loaderData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Result.Success -> {
                        binding.productsListView.hideLoader()
                    }
                    is Result.Loading -> {
                        binding.productsListView.showLoader()
                    }
                    is Error -> {
                        context?.let{
                            shortToast(
                                it,
                                this.getString(R.string.search_error)
                            )
                        }
                        binding.productsListView.hideLoader()
                    }
                }
            }
        )
    }

    override fun loadMore(page: Int) {
        sharedHitsViewModel.loadMore(page)
    }

    override fun loadAgain() {
        sharedHitsViewModel.reloadContent()
    }

    override fun goToItemDetail(data: HitTable) {
        sharedHitsViewModel.saveSelected(data)
        findNavController().navigate(R.id.action_productosListFragment_to_productosDetailFragment)
    }

    override fun deleted(hitItem: HitTable) {
        sharedHitsViewModel.delete(hitItem)
    }
}