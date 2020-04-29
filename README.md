# Samsung Calculator Replica #
This repository stores the code used to create an Android replica of the Samsung Calculator using Kotlin. It will discuss the process of creating the layouts, activities and logic as well as potential improvements if I were to start the project from the start. It will go over every class and layout and how they function. We’ll go through the by their modules in this order:
*   [Main Module](https://github.com/Sgrygorczuk/Android_Calculator/tree/master/app/src/main/java/com/example/calculator/main/ "Main Module")
	* [Main Activity](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/MainActivity.md/ "Main Activity") takes in all of the user inputs passes them to Main Logic for computation or send us to other activities. 
	* [Main Logic](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/MainLogic.md/ "Main Logic") deals with the parsing function and arithmetic computation that gets sent back to Main Activity to be displayed
	* [Main Layout Portrait](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/MainActivityLayoutPort.md/ "Main Layout Portrait") builds the basic layout of a calculator with core being input screen, result screen and keypad for input
	* [Main Layout Landscape](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/MainActivityLayoutLand.md/ "Main Layout Landscape") is an upgraded version of the portrait screen that allows for scientific calculator operations
*   [History Module](https://github.com/Sgrygorczuk/Android_Calculator/tree/master/app/src/main/java/com/example/calculator/history/ "History Module")
	* [History Gradle Project and Gradle Module](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryGradleModuleProject.md/ "History Gradle Project and Gradle Module") shows the prerequisites necessary to be able to work with Room library  
	* [History Entry](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryEntry.md/ "History Entry ") defines the table and columns that will store the user inputs 
	* [History Dao](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryDao.md/ "History Dao ") defines the SQL functions that will be used to interact with out table 
	* [History Database](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryRoomDatabase.md/ "History Database ") 
	* [History Repository](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryRepository.md/ "History Repository") uses the LiveData library to communicate the SQL commands on a separate thread from the one on which the Main Activity runs on
	* [History Adapter](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryRecyclerAdapter.md/ "History Adapter") connects the data from the database to the recycler view to display all user inputs
	* [History Layout](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/HistoryLayout.md/ "History Layout") is the way that history entries will be displayed in the recycler view 
*   [Conversion Module](https://github.com/Sgrygorczuk/Android_Calculator/tree/master/app/src/main/java/com/example/calculator/conversion/ "Conversion Module") | [Spinner Module](https://github.com/Sgrygorczuk/Android_Calculator/tree/master/app/src/main/java/com/example/calculator/spinner/ "Spinner Module")
	* [Conversion Activity](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/ConversionAcitivty.md/ "Conversion Activity") picks the unit type that will be converted, inputs using the keypad and chooses unit using custom spinners. 
	* [Conversion Logic](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/ConversionLogic.md/ "Conversion Logic") deals with the conversion by using large tables. 
	* [Conversion Layout](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/ConversionLayout.md/ "Conversion Layout") allows for choosing of unit type, interaction with spinners and keypad for inputting values. 
	* [Spinner Entry and Adapter](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/ConversionSpinnerEntryAdapter.md/ "Spinner Entry and Adapter")  describes the entries for the spinner and the spinner entries connecting to data relative to their position in the list 
	* [Spinner Layout](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/ConversionSpinnerLayout.md/ "Spinner Layout") 
*   [Tip Module](https://github.com/Sgrygorczuk/Android_Calculator/tree/master/app/src/main/java/com/example/calculator/tip/ "Tip Module")
	* [Tip Activity](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/TipActivity.md/ "Tip Activity") takes input from the keypad, passes them to Tip Logic and displays results. 
	* [Tip Logic](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/TipLogic.md/ "Tip Logic") saves the current user input and calculates the tip and total. 
	* [Tip Layout](https://github.com/Sgrygorczuk/Android_Calculator/blob/master/docs/TipLayout.md/ "Tip Layout") describes the way the tip screen looks 




