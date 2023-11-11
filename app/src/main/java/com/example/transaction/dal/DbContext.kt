package com.example.transaction.dal

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.RoomDatabase
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "transazioni",
    indices =  [
        Index(name = "Cat_Index", value = ["Categoria"]),
        Index(name = "Date_Index", value = ["Data"])])

data class TransactionModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Data")
    val data: String,

    @ColumnInfo(name = "Categoria")
    val category: String,

    @ColumnInfo(name = "Entrata")
    val income: Double,

    @ColumnInfo(name = "Uscita")
    val outcome: Double,

    @ColumnInfo(name = "Descrizione")
    val description: String?,
)


@Parcelize
@Entity(tableName = "categorie")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Nome")
    val name: String,

    @ColumnInfo(name = "Primaria")
    val primary: Int,
) : Parcelable

@Entity(tableName = "transazioni_categorie",
    primaryKeys = ["IdTransazione", "IdCategoria"],
    foreignKeys = [
        ForeignKey(
            entity = TransactionModel::class,
            parentColumns = ["Id"],
            childColumns = ["IdTransazione"],
            onDelete = ForeignKey.NO_ACTION),
        ForeignKey(
            entity = CategoryModel::class,
            parentColumns = ["Id"],
            childColumns = ["IdCategoria"],
            onDelete = ForeignKey.NO_ACTION)
    ])
data class TransactionCategoryModel(
    @ColumnInfo(name = "IdCategoria")
    val IdCategory: Int,

    @ColumnInfo(name = "IdTransazione")
    val IdTransaction: Int,
)

data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionModel,
    @Relation(
        parentColumn = "Id",
        entity = CategoryModel::class,
        entityColumn = "Id",
        associateBy = Junction(
            value = TransactionCategoryModel::class,
            parentColumn = "IdTransazione",
            entityColumn = "IdCategoria"
        )
    )
    val categoryList: List<CategoryModel>
)

@Parcelize
data class ReportModel(
    val category: String?,
    val year: String?,
    val month: String?,
    val income: Double?,
    val outflow: Double?,
    val net: Double?,

) : Parcelable

@Entity(tableName = "saving_accounts")
data class SavingAccountModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Amount")
    val amount: Double,

    @ColumnInfo(name = "Description")
    val description: String?,

    @ColumnInfo(name = "Yield")
    val yield: Double,
)

@Database(entities = [TransactionModel::class, CategoryModel::class, TransactionCategoryModel::class,
    SavingAccountModel::class], version = 1, exportSchema = false)
abstract class DbContext : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): SavingAccountDao
}
