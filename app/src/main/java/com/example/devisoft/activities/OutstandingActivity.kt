package com.example.devisoft.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.devisoft.R
import java.util.*

class OutstandingActivity : AppCompatActivity() {

    private lateinit var dateEditText: EditText
    private lateinit var selectCriteria: EditText
    private lateinit var selectOperator: EditText
    private lateinit var dueDays: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var receivableRadioButton: RadioButton
    private lateinit var payableRadioButton: RadioButton
    private lateinit var generateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.outstanding_form)

        // Set up the app bar title
        val appBarTitle: TextView = findViewById(R.id.appBarTitle)
        appBarTitle.text = "Outstanding"

        val logoImage: ImageView = findViewById(R.id.appBarLogo)

        logoImage.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)

            // Add menu item programmatically
            popupMenu.menu.add(0, 1, 0, "Website") // (groupId, itemId, order, title)

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        val url = "https://devisoft.co.in/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        // Back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Initialize Views
        dateEditText = findViewById(R.id.dateEditText)
        selectCriteria = findViewById(R.id.selectcriteria)
        selectOperator = findViewById(R.id.selectoperator)
        dueDays = findViewById(R.id.duedays)
        radioGroup = findViewById(R.id.radioGroup)
        receivableRadioButton = findViewById(R.id.receivableRadioButton)
        payableRadioButton = findViewById(R.id.payableRadioButton)
        generateButton = findViewById(R.id.button)

        // Date Picker
        dateEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val DRAWABLE_END = 2
                val drawable = dateEditText.compoundDrawables[DRAWABLE_END]
                if (drawable != null &&
                    event.rawX >= (dateEditText.right - drawable.bounds.width() - dateEditText.paddingEnd)
                ) {
                    showDatePicker()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Dropdown for selectCriteria
        selectCriteria.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val DRAWABLE_END = 2
                val drawable = selectCriteria.compoundDrawables[DRAWABLE_END]
                if (drawable != null &&
                    event.rawX >= (selectCriteria.right - drawable.bounds.width() - selectCriteria.paddingEnd)
                ) {
                    showCriteriaDropdown(selectCriteria)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Dropdown for selectOperator
        selectOperator.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val DRAWABLE_END = 2
                val drawable = selectOperator.compoundDrawables[DRAWABLE_END]
                if (drawable != null &&
                    event.rawX >= (selectOperator.right - drawable.bounds.width() - selectOperator.paddingEnd)
                ) {
                    showOperatorDropdown(selectOperator)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Up/Down logic for dueDays EditText
        dueDays.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val DRAWABLE_END = 2
                val drawable = dueDays.compoundDrawables[DRAWABLE_END]
                if (drawable != null &&
                    event.rawX >= (dueDays.right - drawable.bounds.width() - dueDays.paddingEnd)
                ) {
                    val drawableHeight = drawable.bounds.height()
                    val viewHeight = dueDays.height
                    val touchY = event.y
                    val drawableTop = (viewHeight - drawableHeight) / 2f
                    val zoneHeight = drawableHeight / 2f

                    var currentValue = dueDays.text.toString().toIntOrNull() ?: 0

                    if (touchY <= drawableTop + zoneHeight) {
                        currentValue++
                    } else {
                        currentValue--
                    }

                    dueDays.setText(currentValue.toString())
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Generate Button Click
        generateButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val criteria = selectCriteria.text.toString()
            val operator = selectOperator.text.toString()
            val dueDaysValue = dueDays.text.toString()
            val selectedOptionId = radioGroup.checkedRadioButtonId

            when (selectedOptionId) {
                R.id.receivableRadioButton -> {
                    val intent = Intent(this, OutstandingReceivableActivity::class.java)
                    intent.putExtra("date", date)
                    intent.putExtra("criteria", criteria)
                    intent.putExtra("operator", operator)
                    intent.putExtra("dueDays", dueDaysValue)
                    startActivity(intent)
                }
                R.id.payableRadioButton -> {
                    val intent = Intent(this, OutstandingReceivableActivity::class.java)
                    intent.putExtra("date", date)
                    intent.putExtra("criteria", criteria)
                    intent.putExtra("operator", operator)
                    intent.putExtra("dueDays", dueDaysValue)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Please select Receivable or Payable", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                dateEditText.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showCriteriaDropdown(editText: EditText) {
        val options = resources.getStringArray(R.array.criteria_options)
        val popupMenu = PopupMenu(this, editText)
        val menu = popupMenu.menu
        options.forEachIndexed { index, option ->
            menu.add(0, index, 0, option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            editText.setText(menuItem.title)
            true
        }

        popupMenu.show()
    }

    private fun showOperatorDropdown(editText: EditText) {
        val options = arrayOf("=", ">", "<", ">=", "<=")
        val popupMenu = PopupMenu(this, editText)
        val menu = popupMenu.menu
        options.forEachIndexed { index, option ->
            menu.add(0, index, 0, option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            editText.setText(menuItem.title)
            true
        }

        popupMenu.show()
    }
}
