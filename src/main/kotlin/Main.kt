import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.File
import kotlin.system.exitProcess


// See the Oracle JavaFX scrollpane sample
// https://docs.oracle.com/javafx/2/ui_controls/scrollpane.htm

class Main : Application() {
    override fun start(stage: Stage) {

        // you can force the ScrollPane's use of scrollbars
//        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);



        // =========================== CENTRE PANE ========================
        val centrepane = Pane()
        centrepane.prefWidth = 330.0
        centrepane.background = Background(BackgroundFill(Color.valueOf("#f4f4f4"), null, null))
        centrepane.setOnMouseClicked { println("centre pan clicked") }



        /*val circle = Circle(100.0, 150.0, 50.0)
        val rectangle = Rectangle(350.0, 250.0, 75.0, 75.0)
        centrepane.children.add(circle)
        centrepane.children.add(rectangle)

        circle.setOnMousePressed { event -> println("pressed circle"); event.consume() }
        circle.setOnMouseReleased { event -> println("released circle"); event.consume() }
        circle.setOnMouseDragged { event -> println("dragged circle"); event.consume() }

        rectangle.setOnMousePressed { event -> println("pressed rectangle"); event.consume() }
        rectangle.setOnMouseReleased { event -> println("released rectangle"); event.consume() }
        rectangle.setOnMouseDragged { event -> println("dragged rectangle"); event.consume() }

        centrepane.setOnMousePressed { println("pressed pane") }
        centrepane.setOnMouseReleased { println("released pane") }
        centrepane.setOnMouseDragged { println("dragged pane") }
         */

        val scrollPane = ScrollPane()
        scrollPane.content = centrepane








        // define bottom pane
        val bottompane = Pane()
        // track the current path to display in bottom pane
        var pathlabel = Label("")


        // ========================= LEFT PANE =============================
        val leftpane = ListView<String>()

        // track the current path
        var curpath = "${System.getProperty("user.dir")}/test/"

        // track the previous path
        var prevdir = "${System.getProperty("user.dir")}/test/"

        // track the previous item
        var previtem = "test/"

        // track item
        var newitem = ""

        // track home path
        // this is the starting directory
        val homepath = "${System.getProperty("user.dir")}/test/"


        // go through current path and display files in directory
        File(curpath).list().forEach {
            // sort items in list
            leftpane.items.sort()
            // add items to leftpane
            leftpane.items.add(it)
        }


        // use to select a file
        leftpane.selectionModel.selectionMode = SelectionMode.SINGLE
        leftpane.selectionModel.select(0);
        leftpane.setOnMouseClicked {
            println(leftpane.selectionModel.selectedItem)
            // item = file name + type as File
            val item = File(leftpane.selectionModel.selectedItem)
            newitem = leftpane.selectionModel.selectedItem
            println("new item is: $newitem")

            // store as a new path
            var viewing = curpath + item + "/"

            println(item.extension)

            // check if the file format is image (png, jpg, bmp)
            if (item.extension in listOf<String>("png", "jpg", "bmp")) {
                println("image")

                // get the file path of the image selected
                val imgfile = File(viewing)
                val img = Image(viewing)

                // check if image file exists
                if (imgfile.exists()) {
                    println("image file exists: $imgfile")
                    // if so, display in centre pane
                    val iv = ImageView()
                    iv.image = img
                    iv.fitWidth = 330.0
                    iv.isPreserveRatio = true
                    iv.isSmooth = true
                    iv.isCache = true

                    pathlabel = Label(curpath + newitem + "/")

                    // clear everything first
                    centrepane.children.clear()

                    // display new image
                    centrepane.children.add(iv)

                    bottompane.children.clear()

                    bottompane.children.add(pathlabel)

                    println(curpath)

                }

            }
            // check if file format is txt or md
            else if (item.extension in listOf<String>("txt", "md")) {
                println("text")

                // read the text in the file and display
                val bufferedReader: BufferedReader = File(viewing).bufferedReader()
                val inputString = bufferedReader.use { it.readText() }

                val mytext = Label(inputString).apply {
                    padding = Insets(10.0)
                    isWrapText = true
                    maxWidth = 300.0
                }

                val tv = Text(inputString)
                val textFlowPane = TextFlow()
                textFlowPane.setTextAlignment(TextAlignment.LEFT)
                tv.wrappingWidth = 320.0
                tv.y = 20.0
                tv.x = 10.0

                pathlabel = Label(curpath + newitem + "/")

                centrepane.children.clear()

                centrepane.children.add(tv)

                bottompane.children.clear()

                bottompane.children.add(pathlabel)



            }
            // check if file is directory
            else if (item.extension == "") {
                println("directory")
                val newpath = File(viewing)
                println(viewing)
                // delete items of old directory
                File(curpath).list().forEach { leftpane.items.clear() }
                // add items of new directory
                File(viewing).list().forEach {
                    leftpane.items.sort()
                    leftpane.items.add(it)
                }
                // update the current path
                prevdir = curpath
                curpath = viewing

                pathlabel = Label(curpath)

                println("new path is: $viewing")

                bottompane.children.clear()

                bottompane.children.add(pathlabel)
            }
            // if none of the above, then return error
            else {
                println("Unsupported type")
            }

        }








        // ======================= BOTTOM PANE ========================
        //val bottompane = Pane()
        bottompane.prefHeight = 20.0
        bottompane.background = Background(BackgroundFill(Color.valueOf("#ebecf0"), null, null))
        bottompane.setOnMouseClicked { println("bottom pane clicked") }
        pathlabel = Label(curpath + newitem)

        /*
        var item = File(leftpane.selectionModel.selectedItem)


        // if it's a valid file, print new path label
        if (item.extension in listOf<String>("png", "jpg", "bmp", "txt", "md")) {
            // print the new path
            pathlabel = Label(curpath + item + "/")
        }
        // if directory
        else if (item.extension == "") {
            // store prevdir as curpath
            prevdir = curpath
            // set curpath to be new directory
            curpath = curpath + item + "/"
            pathlabel = Label(curpath)
        }
        // if invalid
        else {
            pathlabel = Label("Unsupported type")
        }
        */
        bottompane.children.clear()
        bottompane.children.add(pathlabel)










        // ========================= MENU ============================
        val menubar = MenuBar()
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")
        val fileOpen = MenuItem("Open")
        val fileQuit = MenuItem("Quit")
        fileMenu.items.addAll(fileNew, fileOpen, fileQuit)

        val editMenu = Menu("Edit")
        val editCut = MenuItem("Cut")
        val editCopy = MenuItem("Copy")
        val editPaste = MenuItem("Paste")
        editMenu.items.addAll(editCut, editCopy, editPaste)

        val extraMenu = Menu("Extra")
        val extraToggle = RadioMenuItem("Toggle")
        extraMenu.items.add(extraToggle)

        // Map accelerator keys to menu items
        fileNew.accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)
        fileOpen.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
        fileQuit.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
        editCut.accelerator = KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)
        editCopy.accelerator = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)
        editPaste.accelerator = KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)
        extraToggle.accelerator = KeyCodeCombination(KeyCode.PERIOD, KeyCombination.META_DOWN)

        // Put menus together

        // Put menus together
        menubar.menus.addAll(fileMenu, editMenu, extraMenu)

        // Setup handlers
        fileNew.setOnAction { println("File-New") }
        fileOpen.setOnAction { println("File-Open") }
        fileQuit.setOnAction {
            println("File-Quit")
            exitProcess(0)
        }

        editCut.setOnAction { println("Edit-Cut") }
        editCopy.setOnAction { println("Edit-Copy") }
        editPaste.setOnAction { println("Edit-Paste") }
        extraToggle.setOnAction { println("Extra-Toggle: ${extraToggle.selectedProperty().get()}") }








        // ========================= TOOLBAR ===============================
        /*val newButton = Button("New")
        //newButton.graphic = ImageView(Image("closed.png"))
        newButton.setOnAction { println("New") }

        val openButton = Button("Open")
        //openButton.graphic = ImageView(Image("open.png"))
        openButton.setOnAction { println("Open") }

        val quitButton = Button("Quit")
        //quitButton.graphic = ImageView(Image("quit.png"))
        quitButton.setOnAction {
            println("Quit")
            Platform.exit()
        }*/

        val toolbar = ToolBar()

        val home = Button("Home")
        home.setOnAction {
            println("clicked home")

            File(curpath).list().forEach { leftpane.items.clear() }
            // set current path to home path (test)
            curpath = homepath

            // add items of new directory
            File(curpath).list().forEach {
                leftpane.items.sort()
                leftpane.items.add(it)
            }

            pathlabel = Label(curpath)
            bottompane.children.clear()
            bottompane.children.add(pathlabel)

            centrepane.children.clear()

        }




        val prev = Button("Prev")
        prev.setOnMouseClicked {
            println("Prev clicked")
        }



        val next = Button("Next")
        next.setOnMouseClicked {
            println("clicked next")
            //leftpane.selectionModel.selectionMode = leftpane.selectionModel.select() + 1
        }



        val delete = Button("Delete")
        delete.setOnMouseClicked {
            var curitem = File(leftpane.selectionModel.selectedItem)
            var viewing = curpath + curitem + "/"
            val file = File(viewing)

            val result = file.delete()

            File(curpath).list().forEach { leftpane.items.clear() }


            // add items of new directory
            File(curpath).list().forEach {
                leftpane.items.sort()
                leftpane.items.add(it)
            }

            pathlabel = Label(curpath)
            bottompane.children.clear()
            bottompane.children.add(pathlabel)

            centrepane.children.clear()

            if (result) {
                println("Deleted file")

            }
            else {
                println("Did not delete file")
            }
        }




        val rename = Button("Rename")
        rename.setOnMouseClicked {
            println("Rename clicked")
            /*
            var newname = readLine()
            var newnamefile = File(newname)
            val newdest = curpath + newname + "/"
            var curitem = File(leftpane.selectionModel.selectedItem)
            var viewing = curpath + curitem + "/"
            val file = File(viewing)
            if (!file.exists()) {
                println("File does not exist")
            }
            else if (viewing == newdest) {
                println("File with same name already exists")
            }
            else {
                val rename = file.renameTo(newnamefile)
                if (rename) {
                    println("Renamed File")
                }

                var update = newdest
                File(curpath).list().forEach { leftpane.items.clear() }

                // add items of new directory
                File(curpath).list().forEach {
                    leftpane.items.sort()
                    leftpane.items.add(it)
                }

                pathlabel = Label(update)
                bottompane.children.clear()
                bottompane.children.add(pathlabel)

            }
             */

        }



        // put tools together
        toolbar.items.addAll(home, prev, next, delete, rename)








        // ========================= TOP PANE ========================
        // create a vbox for the top pane
        val toppane = VBox(menubar, toolbar)
        toppane.prefHeight = 30.0
        toppane.background = Background(BackgroundFill(Color.valueOf("#00ffff"), null, null))
        toppane.setOnMouseClicked { println("top pane clicked") }











        // =========================== STAGE ============================
        val root = BorderPane()
        root.left = leftpane
        root.center = scrollPane
        root.top = toppane
        root.bottom = bottompane

        // create the scene and set the stage
        stage.scene = Scene(root, 600.0, 400.0)
        stage.isResizable = false
        stage.title = "File Explorer"
        stage.show()



    }
}
