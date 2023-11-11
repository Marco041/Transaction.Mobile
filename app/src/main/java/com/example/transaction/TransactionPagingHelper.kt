package com.example.transaction

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.transaction.dal.QueryBuilder
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.TransactionWithCategory
import com.example.transaction.dto.TransactionListFilter

class TransactionPagingHelper(
    private val filter: TransactionListFilter?,
    private val repo: TransactionDao
    ) : PagingSource<Int, TransactionWithCategory>() {

    override suspend fun load(params: LoadParams<Int>):  LoadResult<Int, TransactionWithCategory> {
        return try {
            val lastId = params.key ?: (repo.getLastTransactionId() + 1)

            var response: List<TransactionWithCategory>
            if(filter == null) {
                response = repo.getList(params.loadSize, lastId)
            }
            else{
                response = repo.getAll(
                    QueryBuilder.transactionListWithFilter(lastId, params.loadSize, filter))
            }
            //val itemsCount = repo.count()

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.last().transaction.id
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TransactionWithCategory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
