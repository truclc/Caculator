package com.truclc.caculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERATION1 = "Operation1"
private const val STATE_OPERATION_STORED = "Operation1_Stored"

class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        registerListener()
    }

    private fun registerListener() {
        val listener = View.OnClickListener {
            val b = (it as Button).text.toString()
            Toast.makeText(this, "" + b, Toast.LENGTH_SHORT).show()
            edt_input.append(b)
        }
        val opListener = View.OnClickListener {
            val op = (it as Button).text.toString()
            try {
                val value = edt_input.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                edt_input.setText("")
            }
            pendingOperation = op
            tv_operation.text = pendingOperation
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        buttonEquals.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonSub.setOnClickListener(opListener)
        buttonMul.setOnClickListener(opListener)
        buttonDiv.setOnClickListener(opListener)
    }

    private fun performOperation(value: Double, op: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> if (value == 0.0) {
                    operand1 = Double.NaN //divde by zezo
                } else {
                    operand1 = operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
            }
        }
        result.setText(operand1.toString())
        edt_input.setText("")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(STATE_OPERATION_STORED, false)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERATION1)
        } else {
            operand1 = null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "")
        tv_operation.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERATION1, operand1!!)
            outState.putBoolean(STATE_OPERATION_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }
}