package com.michaeludjiawan.moviedb.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.ui.common.BaseFragment
import com.michaeludjiawan.moviedb.util.toVisibility
import kotlinx.android.synthetic.main.favorite_list_fragment.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@InternalCoroutinesApi
class FavoriteListFragment : BaseFragment() {

    private val viewModel by viewModel<FavoriteListViewModel>()

    private val onMovieClick: (Movie) -> Unit = {
        val direction = FavoriteListFragmentDirections.actionFavoriteListFragmentToMovieDetailFragment(it.id, it.title)
        findNavController().navigate(direction)
    }

    private val favoriteAdapter = FavoriteAdapter(onMovieClick)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultToolbar(getString(R.string.favorite_list_toolbar_title))

        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        rv_favorite_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collect { movies ->
                favoriteAdapter.add(movies)

                tv_favorite_empty.visibility = toVisibility(movies.isEmpty())
                rv_favorite_list.visibility = toVisibility(movies.isNotEmpty())
            }
        }
    }
}