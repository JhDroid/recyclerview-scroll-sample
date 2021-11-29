package com.jhdroid.sample.recyclerviewscroll

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.jhdroid.sample.recyclerviewscroll.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val dataList = arrayListOf<String>()
    private var sampleDataAdapter: SampleDataAdapter? = null
    private var isOpen = false // 키보드 올라왔는지 확인

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainActivity = this

        setupView()
        setupAdapter()
    }

    private fun setupView() {
        // 키보드 Open/Close 체크
        binding.clRootContainer.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.clRootContainer.getWindowVisibleDisplayFrame(rect)

            val heightDiff = binding.clRootContainer.height - (rect.bottom - rect.top)
            isOpen = heightDiff > 100 // true == 키보드 올라감
        }
    }

    private fun setupAdapter() {
        sampleDataAdapter = SampleDataAdapter()

        binding.rvDataList.apply {
            adapter = sampleDataAdapter
            addItemDecoration(SpaceDecoration())

            /**
             * 최초 진입 등 데이터가 얼마 없는 경우, StackFromEnd 설정 전까지 해당 Listener에서 스크롤 처리
             * */
            addOnLayoutChangeListener(onLayoutChangeListener)

            /**
             * 키보드가 열리지 않은 상태에서 스크롤 가능 상태이면 StackFromEnd 설정
             * 키보드가 열린 상태에서 체크하면 키보드가 사라질 때 목록이 하단에 붙을 수 있음
             * */
            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isOpen) {
                    setStackFromEnd()
                    removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }
    }

    fun addData(data: String?) {
        if (!data.isNullOrEmpty()) {
            binding.edtInput.setText("")

            dataList.add(data)
            sampleDataAdapter?.submitList(dataList.toList())
            binding.rvDataList.smoothScrollToPosition(dataList.size - 1)
        }
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            // 키보드가 올라와 높이가 변함, StackFromEnd 설정되어있으면 동작하지 않음
//            if (bottom < oldBottom && !binding.rvDataList.getStackFromEnd()) {
            if (bottom < oldBottom) {
                binding.rvDataList.scrollBy(0, oldBottom - bottom) // 스크롤 유지를 위해 추가
            }
        }
}