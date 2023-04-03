//package com.example.infoday
//
//import android.app.Application
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.*
//import androidx.room.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@Dao
//interface ItemDatabaseDao {
//    @Query("SELECT * from event")
//    fun getAll(): LiveData<List<Event>>
//
//    @Query("SELECT * from event where saved = 1")
//    fun getAllSaved(): LiveData<List<Event>>
//
//    @Query("SELECT * from event where deptId = :id")
//    fun getByDeptId(id: String): LiveData<List<Event>>
//
//    @Update
//    suspend fun update(event:Event)
//}
//
//@Database(entities = [Event::class], version = 1)
//abstract class ItemDatabase : RoomDatabase() {
//    abstract fun eventDao(): EventDatabaseDao
//
//    companion object {
//        private var INSTANCE: EventDatabase? = null
//        fun getInstance(context: Context): EventDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        EventDatabase::class.java,
//                        "inventory_database"
//                    )
//                        .createFromAsset("events.db")
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }
//}
//
//class ItemRepository(private val itemDatabaseDao: ItemDatabaseDao) {
//
//    val readAllData: LiveData<List<item>> = ItemDatabaseDao.getAll()
//    val readAllSavedData: LiveData<List<item>> = itemDatabaseDao.getAllSaved()
//
//    suspend fun updateEvent(event: Event) {
//        itemDatabaseDao.update(event)
//    }
//}
//
//
//class ItemViewModel(application: Application): AndroidViewModel(application) {
//
//    val readAllData: LiveData<List<Event>>
//    val readAllSavedData: LiveData<List<Event>>
//
//    private val repository: EventRepository
//
//    init {
//        val eventDao = EventDatabase.getInstance(application).eventDao()
//        repository = EventRepository(eventDao)
//        readAllData = repository.readAllData
//        readAllSavedData = repository.readAllSavedData
//    }
//
//    fun bookmarkEvent(event: Event) {
//        viewModelScope.launch(Dispatchers.IO) {
//            event.saved = true
//            repository.updateEvent(event = event)
////            Log.d("DEBUG", "Added to db")
//        }
//    }
//
//    fun removeEvent(event: Event) {
//        viewModelScope.launch(Dispatchers.IO) {
//            event.saved = false
//            repository.updateEvent(event = event)
//        }
//    }
//
//
//}
//class ItemViewModelFactory(
//    private val application: Application
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
//            return EventViewModel(application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}