/*
 * Copyright 2019 Dash Core Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.schildbach.wallet.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import de.schildbach.wallet.ui.widget.GlobalFooterView
import de.schildbach.wallet_test.R


private const val FINISH_ALL_ACTIVITIES_ACTION = "dash.wallet.action.CLOSE_ALL_ACTIVITIES_ACTION"

@SuppressLint("Registered")
open class GlobalFooterActivity : SessionActivity(), GlobalFooterView.OnFooterActionListener {

    companion object {
        @JvmStatic
        fun finishAll(context: Context) {
            val localIntent = Intent(FINISH_ALL_ACTIVITIES_ACTION)
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
        }
    }

    private lateinit var globalFooterView: GlobalFooterView

    override fun onCreate(savedInstanceState: Bundle?) {
        registerFinishAllReceiver()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unregisterFinishAllReceiver()
        super.onDestroy()
    }

    fun setContentViewWithFooter(layoutResId: Int) {
        globalFooterView = GlobalFooterView.encloseContentView(this, layoutResId)
        setContentView(globalFooterView)
        setupFooter()
    }

    fun setContentViewFooter(layoutResId: Int) {
        super.setContentView(layoutResId)
        globalFooterView = findViewById(R.id.global_footer_view)
        setupFooter()
    }

    private fun setupFooter() {
        globalFooterView.onFooterActionListener = this
    }

    override fun onGotoClick() {
        val intent = PaymentsActivity.createIntent(this, PaymentsActivity.ACTIVE_TAB_RECENT)
        startActivity(intent)
    }

    override fun onMoreClick() {
        val intent = Intent(this, MoreActivity::class.java)
        startActivity(intent)
    }

    override fun onHomeClick() {
        val intent = Intent(this, WalletActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun activateHomeButton() {
        globalFooterView.activateHomeButton(true)
    }

    fun activateMoreButton() {
        globalFooterView.activateMoreButton(true)
    }

    fun activateGotoButton() {
        globalFooterView.activateGotoButton(true)
    }

    private val finishAllReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            LocalBroadcastManager.getInstance(application).unregisterReceiver(this)
            this@GlobalFooterActivity.finish()
        }
    }

    private fun registerFinishAllReceiver() {
        val finishAllFilter = IntentFilter(FINISH_ALL_ACTIVITIES_ACTION)
        LocalBroadcastManager.getInstance(application).registerReceiver(finishAllReceiver, finishAllFilter)
    }

    private fun unregisterFinishAllReceiver() {
        LocalBroadcastManager.getInstance(application).unregisterReceiver(finishAllReceiver)
    }
}