package com.example.passwordgeneratorapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val generateButton: Button = findViewById(R.id.generate_button)
        val copyBtn: Button = findViewById(R.id.copyText_button)
        val copyTxt: TextView = findViewById(R.id.pass_Text)

        //Initializing cpliboardManager and clip data
        var clipBoardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        var clipData: ClipData

        //Action when copy button is clicked
        copyBtn.setOnClickListener {
            val txtCopy = copyTxt!!.text.toString()
            clipData = ClipData.newPlainText("textMy", txtCopy)
            clipBoardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        //When Generate button is clicked I check if all switches are OFF. Is so, a toast warning shows.
        //If at least one of them is ON, generatePass() is executed
        generateButton.setOnClickListener {
            if (switchLowLetters() == false && switchUpLetters() == false && switchNumbers() == false && switchSymbols() == false) {
                Toast.makeText(this, "You must select at least one option please.", Toast.LENGTH_SHORT).show()
            } else {
                generatePass()
            }
        }

        //Set seekBar Listener
        val lengthSizeBar: SeekBar = findViewById<SeekBar>(R.id.lengthSize_seekBar)
        val thisInt: TextView = findViewById(R.id.editTextNumber)
        lengthSizeBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(lengthSizeBar: SeekBar, progress: Int, fromUser: Boolean) {
                thisInt.text = progress.toString()
            }

            override fun onStartTrackingTouch(lengthSizeBar: SeekBar) {
            }

            override fun onStopTrackingTouch(lengthSizeBar: SeekBar) {
                Toast.makeText(this@MainActivity, "Size of password: " + lengthSizeBar.progress, Toast.LENGTH_SHORT).show()
            }

        })
    }

    //This function is executed when Generate button is clicked. It looks for the TextView and edit/inserts generated password
    //Should I put inside this function if statement to see if at least one switch is on?
    private fun generatePass() {
        val resultGeneratePassText: TextView = findViewById(R.id.pass_Text)
        resultGeneratePassText.text = myRandomPass()
        //Toast.makeText(this, "Don't forget your pass!", Toast.LENGTH_SHORT).show()
    }

    //functions that return if switchs are ON or OFF.
    // It would be cool to check them all in one only function.
    fun switchLowLetters():Boolean {
        val myLowLettersSwitch: Switch = findViewById(R.id.lowLetters_switch)
        return myLowLettersSwitch.isChecked()
    }
    fun switchUpLetters():Boolean {
        val myUpLettersSwitch: Switch = findViewById(R.id.upLetters_switch)
        return myUpLettersSwitch.isChecked()
    }
    fun switchNumbers():Boolean {
        val myNumbersSwitch: Switch = findViewById(R.id.numbers_switch)
        return myNumbersSwitch.isChecked()
    }
    fun switchSymbols():Boolean {
        val mySymbolsSwitch: Switch = findViewById(R.id.symbols_switch)
        return mySymbolsSwitch.isChecked()
    }

    //function that determines the Size/Lentght of the password
    // This is to be updated with a slider and a field asking for user input
    fun mySizePass():Int {
        val value: TextView = findViewById(R.id.editTextNumber)
        return value.getText().toString().toInt()
    }

    //My Main Function where password is generated. Wrote this code in IntelliJ and tweaked a little to better suit this app
    fun myRandomPass():String {
        var finalPass: String = ""

        fun randomPass(sizeOfPass: Int = 8, lettersLow: Boolean = true, lettersUp: Boolean = true, numbers: Boolean = true, symbols: Boolean = true): String {

            //Declare list of characters to create password
            val listAbc = listOf<Char>('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                    's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
            val listNumbers = listOf<Char>('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            val listSymbols = listOf<Char>('!', '?', '(', ')', '[', ']', '{', '}', '"', '<', '>', '"', ':', ';', '.', ',',
                    '-', '_', '|', '^', '~', '@', '&', '$')

            //Declare needed variables and initialize them
            val tempListRandChar = mutableListOf<Char>()
            val tempListPassChar = mutableListOf<Char>()

            //Loop that creates random chars list to generate a password.
            var count: Int = 0
            while (count < sizeOfPass ) {
                //First I create a small list with an element from each different list of chars
                if (lettersLow == false) {
                    print("")
                } else {
                    val a = listAbc.shuffled()
                    tempListRandChar.add(a.random())
                }

                if (lettersUp == false) {
                    print("")
                } else {
                    val b = listAbc.shuffled()
                    tempListRandChar.add(b.random().toUpperCase())
                }

                if (numbers == false) {
                    print("")
                } else {
                    val c = listNumbers.shuffled()
                    tempListRandChar.add(c.random())
                }

                if (symbols == false) {
                    print("")
                } else {
                    val d = listSymbols.shuffled()
                    tempListRandChar.add(d.random())
                }
                //Than from that small list I randomized it again an add that element to final char list
                //val randomChar: Int = (0 until tempListRandChar.size).random()
                tempListPassChar.add(tempListRandChar[(0 until tempListRandChar.size).random()])
                tempListRandChar.clear()
                count += 1
            }

            //Transform to a CharArray to be able to print a string out of the mutableLisOf<Char>
            finalPass = String(tempListPassChar.toCharArray())
            return finalPass
        }
        //passes all user options as arguments for randomPass() and returns value
        return randomPass(mySizePass(), switchLowLetters(), switchUpLetters(), switchNumbers(), switchSymbols())
    }

}