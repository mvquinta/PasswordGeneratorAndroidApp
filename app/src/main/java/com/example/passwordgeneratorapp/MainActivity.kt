package com.example.passwordgeneratorapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.passwordgeneratorapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var finalPass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val generateButton: Button = binding.generateButton
        val copyBtn: Button = binding.copyTextButton
        val copyTxt: TextView = binding.passText
        generatePass()

        //Initializing clipBoardManager and clip data
        //Code from geeksforgeeks.org - clipboard-in-android
        var clipBoardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        var clipData: ClipData

        //Action when copy button is clicked
        copyBtn.setOnClickListener {
            // Text from the edit text is stored in a val
            val txtCopy = copyTxt!!.text.toString()
            // clip data is initialized with the text variable declared above
            clipData = ClipData.newPlainText("textMy", txtCopy)
            // Clipboard saves this clip object
            clipBoardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        //When Generate button is clicked I check if all switches are OFF. Is so, a toast warning shows.
        //If at least one of them is ON, generatePass() is executed
        generateButton.setOnClickListener {
            //if (switchLowLetters() == false && switchUpLetters() == false && switchNumbers() == false && switchSymbols() == false) {
            if (passStrengthSwitchLevel() == 0) {
                Toast.makeText(this, "You must select at least one option please.", Toast.LENGTH_SHORT).show()
            } else {
                generatePass()
            }
        }


        //Set seekBar Listener
        val lengthSizeBar: SeekBar = binding.lengthSizeSeekBar
        val thisInt: TextView = binding.lengthPassText
        val passStrengthWarning = binding.passStrengthText

        lengthSizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("NewApi")
            override fun onProgressChanged(lengthSizeBar: SeekBar, progress: Int, fromUser: Boolean) {
                thisInt.text = progress.toString()

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_weak))
                            lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_weak))
                            }*/

                val myProgressiveNum = passStrengthLevel(thisInt.text.toString().toInt())
                when (myProgressiveNum) {
                    in 2..24 -> {
                        passStrengthWarning.text = getString(R.string.passStrength_weak)
                        passStrengthWarning.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_weak))
                        lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_weak))
                        lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_weak))
                    }
                    in 25..49 -> {
                        passStrengthWarning.text = getString(R.string.passStrength_good)
                        passStrengthWarning.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_good))
                        lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_good))
                        lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_good))
                    }
                    in 50..89 -> {
                        passStrengthWarning.text = getString(R.string.passStrength_strong)
                        passStrengthWarning.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_strong))
                        lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_strong))
                        lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_strong))
                    }
                    in 90..1000 -> {
                        passStrengthWarning.text = getString(R.string.passStrength_very_strong)
                        passStrengthWarning.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_strong))
                        lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_strong))
                        lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_strong))
                    }
                    else -> {
                        passStrengthWarning.text = getString(R.string.passStrength_very_weak)
                        passStrengthWarning.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_weak))
                        lengthSizeBar.thumb.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_weak))
                        lengthSizeBar.progressDrawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.passStrength_color_very_weak))
                    }
                }
            }

            override fun onStartTrackingTouch(lengthSizeBar: SeekBar) {
            }

            override fun onStopTrackingTouch(lengthSizeBar: SeekBar) {
                Toast.makeText(this@MainActivity, "Size of password: " + lengthSizeBar.progress, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //This function is executed when Generate button is clicked. It looks for the TextView and edit/inserts generated password
    private fun generatePass() {
        val resultGeneratePassText: TextView = binding.passText
        resultGeneratePassText.text = randomPass()
    }

    //This fun() checks how many switch are ON and saves it to an Int variable.
    //If all switchs are off, numSwitchesOn = 0, I wont be able to create a password
    //If numSwitchesOn = 1, 2, 3 or 4 it means, respectively, a weak, good, strong and very strong password
    private fun passStrengthSwitchLevel(): Int {
        val myLowLettersSwitch: Switch = binding.lowLettersSwitch
        val myUpLettersSwitch: Switch = binding.upLettersSwitch
        val myNumbersSwitch: Switch = binding.numbersSwitch
        val mySymbolsSwitch: Switch = binding.symbolsSwitch
        var numSwitchesOn:Int = 0
        val mySwitchList = listOf<Switch>(myLowLettersSwitch, myUpLettersSwitch, myNumbersSwitch, mySymbolsSwitch)

        for (i in mySwitchList) {
            if (i.isChecked() == true) {
                numSwitchesOn = numSwitchesOn + 1
            }
        }
        return numSwitchesOn
    }

    /*In this fun() I calculate a number that will serve me as strength level analyser.
    Having into account the length/size of the password and the number of switches on, I created a simple calculation that tells me if it's a weak, good, strong or very strong password.
    This fun() is called in seekBar onProgressChanged. That way I can pass in real time to the function as an argument the length selected by user*/
    private fun passStrengthLevel(realTimeLength: Int): Int {
        var realTimeStrengthLevel: Int = 1
        if ( passStrengthSwitchLevel() == 2) {
            realTimeStrengthLevel = 16 + realTimeLength * 2
        } else if ( passStrengthSwitchLevel() == 3) {
            realTimeStrengthLevel = 32 + realTimeLength * 2
        } else if ( passStrengthSwitchLevel() == 4) {
            realTimeStrengthLevel = 50 + realTimeLength * 2
        } else {
            realTimeStrengthLevel = passStrengthSwitchLevel()
        }
        return realTimeStrengthLevel
    }

    //Functions that return if switch are ON or OFF.
    // It would be cool to check them all in one only function.
    private fun switchLowLetters():Boolean {
        val myLowLettersSwitch: Switch = binding.lowLettersSwitch
        return myLowLettersSwitch.isChecked()
    }
    private fun switchUpLetters():Boolean {
        val myUpLettersSwitch: Switch = binding.upLettersSwitch
        return myUpLettersSwitch.isChecked()
    }
    private fun switchNumbers():Boolean {
        val myNumbersSwitch: Switch = binding.numbersSwitch
        return myNumbersSwitch.isChecked()
    }
    private fun switchSymbols():Boolean {
        val mySymbolsSwitch: Switch = binding.symbolsSwitch
        return mySymbolsSwitch.isChecked()
    }

    //Function that determines the Size/Length of the password
    private fun mySizePass():Int {
        val value: TextView = binding.lengthPassText
        return value.getText().toString().toInt()
    }

    //My Main Function where password is generated. Wrote this code in IntelliJ and tweaked a little to better suit this app
    private fun randomPass(sizeOfPass: Int = mySizePass(), lettersLow: Boolean = switchLowLetters(), lettersUp: Boolean = switchUpLetters(), numbers: Boolean = switchNumbers(), symbols: Boolean = switchSymbols()): String {

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
}