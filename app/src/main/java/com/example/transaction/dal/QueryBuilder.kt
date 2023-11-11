package com.example.transaction.dal

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.transaction.dto.ReportFilter
import com.example.transaction.dto.TransactionListFilter


class QueryBuilder {
    companion object {
        fun transactionListWithFilter(
            lastId: Int,
            take: Int,
            filterObj: TransactionListFilter?
        ): SimpleSQLiteQuery {
            val query = "" +
                    "SELECT * " +
                    "FROM Transazioni " +
                    "WHERE Id < ? --{whereClause}" +
                    " ORDER BY Id DESC" +
                    " LIMIT ?"

            var whereClause = ""
            val args: ArrayList<Any?> = ArrayList()
            args.add(lastId)

            if (filterObj != null) {
                if (!filterObj.textFilter.isNullOrBlank()) {
                    whereClause += " AND (Categoria like ? OR Descrizione like ?)"
                    args.add(filterObj.textFilter+"%")
                    args.add(filterObj.textFilter+"%")
                }

                if (!filterObj.monthFilter.isNullOrBlank()) {
                    whereClause += " AND strftime('%m', Data) = ?"
                    args.add(filterObj.monthFilter)
                }

                if (!filterObj.yearFitler.isNullOrBlank()) {
                    whereClause += " AND strftime('%Y', Data) = ?"
                    args.add(filterObj.yearFitler)
                }
            }

            args.add(take)
            val result = SimpleSQLiteQuery(
                query.replace("--{whereClause}", whereClause),
                args.toArray());
            return result
        }

        fun categoryReport(filter: ReportFilter?): SimpleSQLiteQuery{
            var baseQuery = """
                SELECT c.nome AS category,  
                    SUM(Entrata) AS income,
                    SUM(Uscita) AS outflow,
                    SUM(Entrata)-SUM(Uscita) as net
                    --{yearSelect}
                FROM transazioni t
                JOIN transazioni_categorie tc ON t.id=tc.IdTransazione
                JOIN categorie c ON c.id=tc.IdCategoria --{onlyPrimaryCategory}
                --{categoryFilter}
                GROUP BY c.Nome --{yearAggregation}
                --{orderBy}"""

            val args: ArrayList<Any?> = ArrayList()

            if(filter != null){
                if(!filter.splitBySubcategory) {
                    baseQuery = baseQuery.replace("--{onlyPrimaryCategory}", " AND Primaria = 1")
                }

                if(filter.splitByYear){
                    baseQuery = baseQuery
                        .replace("--{yearSelect}", ", strftime('%Y', Data) AS year")
                        .replace("--{yearAggregation}", ", strftime('%Y', Data)")
                        .replace("--{orderBy}", "ORDER BY year DESC, category")
                }
                else{
                    baseQuery.replace("--{orderBy}", "ORDER BY net DESC")
                }

                if(!filter.categoryFilter.isNullOrEmpty()){
                    baseQuery = baseQuery.replace("--{categoryFilter}", " WHERE c.Nome LIKE ?")
                    args.add("%"+filter.categoryFilter+"%")
                }
            }

            val result = SimpleSQLiteQuery(
                baseQuery,
                args.toArray());
            return result
        }

        fun getCheckpointQuery(): SimpleSQLiteQuery{
            return SimpleSQLiteQuery("pragma wal_checkpoint(full)")
        }
    }
}