package com.zeal.paymentassignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.zeal.paymentassignment.R
import com.zeal.paymentassignment.core.FlowDataObject
import com.zeal.paymentassignment.databinding.FragmentEnterAmount2Binding

class EnterAmountDataFragment : Fragment() {
    val binding by lazy {
        FragmentEnterAmount2Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            val amount = binding.tvEnterAmount.text.toString()
            if (amount.isNotBlank()) {
                try {
                    val amountF = amount.toFloat()
                    if (amountF == 0.0f) {
                        Toast.makeText(context, "Can't be zero", Toast.LENGTH_SHORT).show()
                    } else {
                        FlowDataObject.getInstance().amount = amountF

                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            `package` = "com.example.bank"
                            putExtra(Intent.EXTRA_TEXT, amountF.toString())
                            type = "text/plain"
                        }

                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivityForResult(intent, REQUEST_CODE)
                        } else {
                            Toast.makeText(context, "Bank app not available", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Please add a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                val processedAmount = it.toFloat()
                Toast.makeText(context, "Amount After Discount: $processedAmount", Toast.LENGTH_SHORT)
                    .show()
                FlowDataObject.getInstance().amount = it.toFloat()
                findNavController().navigate(R.id.action_enterAmountDataFragment_to_swipeCardFragment)
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}