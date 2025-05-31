package org.fossify.commons.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import org.fossify.commons.R
import org.fossify.commons.databinding.ItemBreadcrumbBinding
import org.fossify.commons.extensions.adjustAlpha
import org.fossify.commons.extensions.getBasePath
import org.fossify.commons.extensions.getDialogBackgroundColor
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.humanizePath
import org.fossify.commons.extensions.onGlobalLayout
import org.fossify.commons.extensions.setDrawablesRelativeWithIntrinsicBounds
import org.fossify.commons.helpers.MEDIUM_ALPHA
import org.fossify.commons.models.FileDirItem

class Breadcrumbs(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val itemsLayout: LinearLayout
    private var textColor = context.getProperTextColor()
    private var accentColor = context.getProperPrimaryColor()
    private var fontSize = resources.getDimension(R.dimen.bigger_text_size)
    private var lastPath = ""
    private var isLayoutDirty = true
    private var isScrollToSelectedItemPending = false
    private var isFirstScroll = true
    private var stickyRootInitialLeft = 0
    private var rootStartPadding = 0

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
        rootStartPadding = paddingStart
        itemsLayout.setPaddingRelative(0, paddingTop, paddingEnd, paddingBottom)
        setPaddingRelative(0, 0, 0, 0)
        addView(itemsLayout, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        onGlobalLayout {
            stickyRootInitialLeft = if (itemsLayout.childCount > 0) {
                itemsLayout.getChildAt(0).left
            } else {
                0
            }
        }
    }

    private fun recomputeStickyRootLocation(left: Int) {
        stickyRootInitialLeft = left
        handleRootStickiness(scrollX)
    }

    private fun handleRootStickiness(scrollX: Int) {
        if (scrollX > stickyRootInitialLeft) {
            stickRoot(scrollX - stickyRootInitialLeft)
        } else {
            freeRoot()
        }
    }

    private fun freeRoot() {
        if (itemsLayout.childCount > 0) {
            itemsLayout.getChildAt(0).translationX = 0f
        }
    }

    private fun stickRoot(translationX: Int) {
        if (itemsLayout.childCount > 0) {
            val root = itemsLayout.getChildAt(0)
            root.translationX = translationX.toFloat()
            ViewCompat.setTranslationZ(root, translationZ)
        }
    }

    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
        handleRootStickiness(scrollX)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        isLayoutDirty = false
        if (isScrollToSelectedItemPending) {
            scrollToSelectedItem()
            isScrollToSelectedItemPending = false
        }

        recomputeStickyRootLocation(left)
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
                addBreadcrumb(
                    item = item,
                    index = i,
                    isLast = item.path.trimEnd('/') == lastPath.trimEnd('/')
                )
                scrollToSelectedItem()
            }
    }

    private fun addBreadcrumb(item: FileDirItem, index: Int, isLast: Boolean) {
        ItemBreadcrumbBinding.inflate(inflater, itemsLayout, false).apply {
            breadcrumbText.isActivated = isLast && index != 0

            breadcrumbText.text = item.name
            breadcrumbText.setTextColor(textColorStateList)
            breadcrumbText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

            itemsLayout.addView(root)
            if (index > 0) {
                breadcrumbText.setDrawablesRelativeWithIntrinsicBounds(
                    start = AppCompatResources.getDrawable(
                        context, R.drawable.ic_chevron_right_vector
                    )
                )

                TextViewCompat.setCompoundDrawableTintList(breadcrumbText, textColorStateList)
            } else {
                breadcrumbText.elevation = context.resources.getDimension(R.dimen.one_dp)
                setupStickyBreadcrumbBackground(breadcrumbText)
                val horizontalPadding =
                    context.resources.getDimensionPixelSize(R.dimen.normal_margin)
                breadcrumbText.updatePadding(left = horizontalPadding, right = horizontalPadding)
                setPadding(rootStartPadding, 0, 0, 0)
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

    private fun setupStickyBreadcrumbBackground(view: MyTextView) {
        val drawable = view.background.mutate() as RippleDrawable
        (drawable.getDrawable(0) as GradientDrawable)
            .apply {
                setColor(context.getDialogBackgroundColor())
                setStroke(
                    context.resources.getDimensionPixelSize(R.dimen.one_dp),
                    context.getProperPrimaryColor().adjustAlpha(MEDIUM_ALPHA)
                )
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
