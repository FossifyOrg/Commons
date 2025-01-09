package org.fossify.commons.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import org.fossify.commons.R
import org.fossify.commons.databinding.ItemBreadcrumbBinding
import org.fossify.commons.extensions.applyColorFilter
import org.fossify.commons.extensions.getBasePath
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.humanizePath
import org.fossify.commons.models.FileDirItem

class Breadcrumbs(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val itemsLayout: LinearLayout
    private var textColor = context.getProperTextColor()
    private var accentColor = context.getProperPrimaryColor()
    private var fontSize = resources.getDimension(R.dimen.bigger_text_size)
    private var lastPath = ""
    private var isLayoutDirty = true
    private var isScrollToSelectedItemPending = false
    private var isFirstScroll = true

    private val textColorStateList: ColorStateList
        get() = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_activated), intArrayOf()),
            intArrayOf(
                accentColor,
                textColor
            )
        )

    var listener: BreadcrumbsListener? = null
    var isShownInDialog = false

    init {
        isHorizontalScrollBarEnabled = false
        itemsLayout = LinearLayout(context)
        itemsLayout.orientation = LinearLayout.HORIZONTAL
        itemsLayout.setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
        setPaddingRelative(0, 0, 0, 0)
        addView(itemsLayout, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        isLayoutDirty = false
        if (isScrollToSelectedItemPending) {
            scrollToSelectedItem()
            isScrollToSelectedItemPending = false
        }
    }

    private fun scrollToSelectedItem() {
        if (isLayoutDirty) {
            isScrollToSelectedItemPending = true
            return
        }

        var selectedIndex = itemsLayout.childCount - 1
        val cnt = itemsLayout.childCount
        for (i in 0 until cnt) {
            val child = itemsLayout.getChildAt(i)
            if ((child.tag as? FileDirItem)?.path?.trimEnd('/') == lastPath.trimEnd('/')) {
                selectedIndex = i
                break
            }
        }

        val selectedItemView = itemsLayout.getChildAt(selectedIndex)
        val scrollX = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            selectedItemView.left - itemsLayout.paddingStart
        } else {
            selectedItemView.right - width + itemsLayout.paddingStart
        }

        if (!isFirstScroll && isShown) {
            smoothScrollTo(scrollX, 0)
        } else {
            scrollTo(scrollX, 0)
        }

        isFirstScroll = false
    }

    override fun requestLayout() {
        isLayoutDirty = true
        super.requestLayout()
    }

    fun setBreadcrumb(fullPath: String) {
        lastPath = fullPath
        val basePath = fullPath.getBasePath(context)
        var currPath = basePath
        val tempPath = context.humanizePath(fullPath)

        itemsLayout.removeAllViews()
        tempPath.split("/")
            .dropLastWhile(String::isEmpty)
            .forEachIndexed { i, dir ->
                if (i > 0) {
                    currPath += "$dir/"
                }
                if (dir.isEmpty()) {
                    return@forEachIndexed
                }
                currPath = "${currPath.trimEnd('/')}/"
                val item = FileDirItem(currPath, dir, true, 0, 0, 0)
                addBreadcrumb(item = item, index = i, isLast = item.path.trimEnd('/') == lastPath.trimEnd('/'))
                scrollToSelectedItem()
            }
    }

    private fun addBreadcrumb(item: FileDirItem, index: Int, isLast: Boolean) {
        ItemBreadcrumbBinding.inflate(inflater, itemsLayout, false).apply {
            breadcrumbText.isActivated = isLast

            breadcrumbText.text = item.name
            breadcrumbText.setTextColor(textColorStateList)
            breadcrumbText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

            itemsLayout.addView(root)
            if (index > 0) {
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(breadcrumbText, R.drawable.ic_chevron_right_vector, 0, 0, 0)
                TextViewCompat.setCompoundDrawableTintList(breadcrumbText, textColorStateList)
            } else {
                breadcrumbText.background = ContextCompat.getDrawable(context, R.drawable.button_background)
                breadcrumbText.background.applyColorFilter(textColor)
                val horizontalPadding = context.resources.getDimensionPixelSize(R.dimen.normal_margin)
                breadcrumbText.updatePadding(left = horizontalPadding, right = horizontalPadding)
            }

            breadcrumbText.setOnClickListener { v ->
                if (itemsLayout.getChildAt(index) != null && itemsLayout.getChildAt(index) == v) {
                    if (index != 0 && isLast) {
                        scrollToSelectedItem()
                    } else {
                        listener?.breadcrumbClicked(index)
                    }
                }
            }

            root.tag = item
        }
    }

    fun updateColor(color: Int) {
        textColor = color
        setBreadcrumb(lastPath)
    }

    fun updateFontSize(size: Float, updateTexts: Boolean) {
        fontSize = size
        if (updateTexts) {
            setBreadcrumb(lastPath)
        }
    }

    fun removeBreadcrumb() {
        itemsLayout.removeView(itemsLayout.getChildAt(itemsLayout.childCount - 1))
    }

    fun getItem(index: Int) = itemsLayout.getChildAt(index).tag as FileDirItem

    fun getLastItem() = itemsLayout.getChildAt(itemsLayout.childCount - 1).tag as FileDirItem

    fun getItemCount() = itemsLayout.childCount

    interface BreadcrumbsListener {
        fun breadcrumbClicked(id: Int)
    }
}
