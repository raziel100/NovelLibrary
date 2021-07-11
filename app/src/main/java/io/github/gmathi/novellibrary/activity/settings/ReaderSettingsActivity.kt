package io.github.gmathi.novellibrary.activity.settings

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import io.github.gmathi.novellibrary.R
import io.github.gmathi.novellibrary.activity.BaseActivity
import io.github.gmathi.novellibrary.adapter.GenericAdapter
import io.github.gmathi.novellibrary.databinding.ActivitySettingsBinding
import io.github.gmathi.novellibrary.databinding.ListitemTitleSubtitleWidgetBinding
import io.github.gmathi.novellibrary.model.ui.*
import io.github.gmathi.novellibrary.util.FAC
import io.github.gmathi.novellibrary.util.Constants.VOLUME_SCROLL_LENGTH_MAX
import io.github.gmathi.novellibrary.util.Constants.VOLUME_SCROLL_LENGTH_MIN
import io.github.gmathi.novellibrary.util.view.setDefaults
import io.github.gmathi.novellibrary.util.system.startReaderBackgroundSettingsActivity
import io.github.gmathi.novellibrary.util.view.CustomDividerItemDecoration
import io.github.gmathi.novellibrary.util.view.TwoWaySeekBar
import java.io.File
import kotlin.math.abs

class ReaderSettingsActivity : BaseActivity(), GenericAdapter.Listener<Setting> {

    companion object {
        private val OPTIONS = listOf(
            Setting(R.string.reader_mode, R.string.reader_mode_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.readerMode) { _, value ->
                    dataCenter.readerMode = value
                    if (value) {
                        dataCenter.javascriptDisabled = value
                        adapter.notifyDataSetChanged()
                    }
                }
            },
            Setting(R.string.disable_javascript, R.string.disable_javascript_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.javascriptDisabled) { _, value ->
                    dataCenter.javascriptDisabled = value
                    if (!value) {
                        dataCenter.readerMode = value
                        adapter.notifyDataSetChanged()
                    }
                }
            },
            Setting(R.string.reader_mode_colors, R.string.reader_mode_colors_description).bindChevron { _, _ ->
                startReaderBackgroundSettingsActivity()
            },
            Setting(R.string.swipe_right_for_next_chapter, R.string.swipe_right_for_next_chapter_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.japSwipe) { _, value -> dataCenter.japSwipe = value }
            },
            Setting(R.string.show_reader_scroll, R.string.show_reader_scroll_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.showReaderScroll) { _, value -> dataCenter.showReaderScroll = value }
            },
            Setting(R.string.show_comments_section, R.string.show_comments_section_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.showChapterComments) { _, value -> dataCenter.showChapterComments = value }
            },
            Setting(R.string.volume_scroll, R.string.volume_scroll_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.volumeScroll) { _, value ->
                    dataCenter.volumeScroll = value
                    adapter.notifyDataSetChanged()
                }
            },
            Setting(R.string.volume_scroll_length, R.string.volume_scroll_length_description).onBind { _, view, _ ->
                val enable = dataCenter.volumeScroll
                //itemView.enabled(enable)
                view.blackOverlay.visibility =
                    if (enable)
                        View.INVISIBLE
                    else
                        View.VISIBLE
                view.currentValue.visibility = View.VISIBLE
                val value = dataCenter.scrollLength
                view.currentValue.text = resources.getString(R.string.volume_scroll_length_template,
                    if (value < 0) resources.getString(R.string.reverse) else "",
                    abs(value)
                )
                if (enable)
                    view.root.setOnClickListener {
                        changeScrollDistance(view.currentValue)
                    }
                else view.root.setOnClickListener(null)
            },
            Setting(R.string.keep_screen_on, R.string.keep_screen_on_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.keepScreenOn) { _, value -> dataCenter.keepScreenOn = value }
            },
            Setting(R.string.immersive_mode, R.string.immersive_mode_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.enableImmersiveMode) { _, value -> dataCenter.enableImmersiveMode = value }
            },
            Setting(R.string.show_navbar_at_chapter_end, R.string.show_navbar_at_chapter_end_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.showNavbarAtChapterEnd) { _, value -> dataCenter.showNavbarAtChapterEnd = value }
            },
            Setting(R.string.merge_pages, R.string.merge_pages_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.enableClusterPages) { _, value -> dataCenter.enableClusterPages = value }
            },
            Setting(R.string.directional_links, R.string.directional_links_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.enableDirectionalLinks) { _, value -> dataCenter.enableDirectionalLinks = value }
            },
            Setting(R.string.reader_mode_button_visibility, R.string.reader_mode_button_visibility_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.isReaderModeButtonVisible) { _, value -> dataCenter.isReaderModeButtonVisible = value }
            },
            Setting(R.string.keep_text_color, R.string.keep_text_color_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.keepTextColor) { _, value -> dataCenter.keepTextColor = value }
            },
            Setting(R.string.alternative_text_colors, R.string.alternative_text_colors_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.alternativeTextColors) { _, value -> dataCenter.alternativeTextColors = value }
            },
            Setting(R.string.limit_image_width, R.string.limit_image_width_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.limitImageWidth) { _, value -> dataCenter.limitImageWidth = value }
            },
            Setting(R.string.auto_read_next_chapter, R.string.auto_read_next_chapter_description).onBind { _, view, _ ->
                view.bindSwitch(dataCenter.readAloudNextChapter) { _, value -> dataCenter.readAloudNextChapter = value }
            },
            Setting(R.string.custom_query_lookups, R.string.custom_query_lookups_description).bindChevron { _, _ ->
                MaterialDialog(this).show {
                    title(R.string.custom_query_lookups_edit)
                    input(
                        hintRes = R.string.custom_query_lookups_hint,
                        prefill = dataCenter.userSpecifiedSelectorQueries,
                        inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE + InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE + InputType.TYPE_CLASS_TEXT
                    )
                    positiveButton(R.string.fui_button_text_save) { widget ->
                        dataCenter.userSpecifiedSelectorQueries = widget.getInputField().text.toString()
                        firebaseAnalytics.logEvent(FAC.Event.SELECTOR_QUERY) {
                            param(FirebaseAnalytics.Param.VALUE, widget.getInputField().text.toString())
                        }
                    }
                    negativeButton(R.string.cancel)
                }
            },
        )
    }

    lateinit var adapter: GenericAdapter<Setting>

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        adapter = GenericAdapter(items = ArrayList(OPTIONS), layoutResId = R.layout.listitem_title_subtitle_widget, listener = this)
        binding.contentRecyclerView.recyclerView.setDefaults(adapter)
        binding.contentRecyclerView.recyclerView.addItemDecoration(CustomDividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.contentRecyclerView.swipeRefreshLayout.isEnabled = false
    }

    override fun bind(item: Setting, itemView: View, position: Int) {
        val itemBinding = ListitemTitleSubtitleWidgetBinding.bind(itemView)
        fillSettingDefaults(itemBinding, resources.getString(item.name), resources.getString(item.description), position)

        OPTIONS.getOrNull(position)?.bindCallback?.let { it(this, item, itemBinding, position) }
    }

    override fun onItemClick(item: Setting, position: Int) {
//        if (item == getString(R.string.sync_interval)) {
//            showSyncIntervalDialog()
//        }
        OPTIONS.getOrNull(position)?.clickCallback?.let { it(this, item, position) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Delete Files
    private fun deleteFilesDialog() {
        MaterialDialog(this).show {
            title(R.string.clear_data)
            message(R.string.clear_data_description)
            positiveButton(R.string.clear) { dialog ->
                val snackBar = Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.clearing_data) + " - " + getString(R.string.please_wait),
                    Snackbar.LENGTH_INDEFINITE
                )
                deleteFiles()
                snackBar.dismiss()
                dialog.dismiss()
            }
            negativeButton(R.string.cancel) { dialog ->
                dialog.dismiss()
            }
        }
    }

    private fun deleteFiles() {
        try {
            deleteDir(cacheDir)
            deleteDir(filesDir)
            dbHelper.removeAll()
            dataCenter.saveNovelSearchHistory(ArrayList())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                deleteDir(File(dir, children[i]))
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
    //endregion

    private fun changeScrollDistance(textView: TextView) {
        var value = dataCenter.scrollLength

        val dialog = MaterialDialog(this).show {
            title(R.string.volume_scroll_length)
            customView(R.layout.dialog_slider, scrollable = true)
            onDismiss {
                dataCenter.scrollLength = value
            }
        }

        val seekBar = dialog.getCustomView().findViewById<TwoWaySeekBar>(R.id.seekBar) ?: return
        seekBar.notifyWhileDragging = true
        seekBar.setOnSeekBarChangedListener { _, progress ->
            value = progress.toInt()
            textView.text = resources.getString(R.string.volume_scroll_length_template,
                if (value < 0) resources.getString(R.string.reverse) else "",
                abs(value)
            )
        }
        seekBar.setAbsoluteMinMaxValue(VOLUME_SCROLL_LENGTH_MIN.toDouble(), VOLUME_SCROLL_LENGTH_MAX.toDouble())
        seekBar.setProgress(dataCenter.scrollLength.toDouble())
    }

}

private typealias Setting = GenericSettingItem<ReaderSettingsActivity>