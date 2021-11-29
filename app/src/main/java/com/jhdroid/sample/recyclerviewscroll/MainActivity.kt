package com.jhdroid.sample.recyclerviewscroll

import android.graphics.Rect
import android.os.Bundle
import android.view.View
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
        // 화면 진입 시 데이터 설정 후 스크롤 테스트
//        setupData()
    }

    private fun setupView() {
        // 키보드 Open/Close 체크
        binding.clRootContainer.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.clRootContainer.getWindowVisibleDisplayFrame(rect)

            val rootViewHeight = binding.clRootContainer.rootView.height
            val heightDiff = rootViewHeight - rect.height()
            isOpen = heightDiff > rootViewHeight * 0.25 // true == 키보드 올라감
        }
    }

    private fun setupAdapter() {
        sampleDataAdapter = SampleDataAdapter()

        binding.rvDataList.apply {
            adapter = sampleDataAdapter
            addItemDecoration(SpaceDecoration())

            /**
             * 1. 키보드가 올라온 경우에만 스크롤이 가능한 경우 처리
             * 키보드가 내려간 경우 스크롤이 불가능하지만 키보드가 올라오면서 스크롤이 가능한 경우
             * */
            addOnLayoutChangeListener(onLayoutChangeListener)

            /**
             * 2. 키보드가 올라온 상태에서 데이터를 추가해 키보드가 내려갔을 때에도 스크롤이 가능한 경우
             * 3. 화면 진입 시 데이터를 불러와 청므부터 스크롤이 가능한 경우
             * 키보드가 열리지 않은 상태에서 스크롤 가능 상태이면 StackFromEnd 설정
             * 키보드가 열린 상태에서 체크하면 키보드가 사라질 때 목록이 하단에 붙을 수 있음
             * */
            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isOpen) { // 스크롤이 가능하면서 키보드가 닫힌 상태일 떄만
                    setStackFromEnd()
                    removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }
    }

    private fun setupData() { // 더미 데이터 설정
        for (i in 1..20) dataList.add(i.toString())

        sampleDataAdapter?.submitList(dataList.toList())
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
            // 키보드가 올라와 높이가 변함
            if (bottom < oldBottom) {
                binding.rvDataList.scrollBy(0, oldBottom - bottom) // 스크롤 유지를 위해 추가
            }
        }
}