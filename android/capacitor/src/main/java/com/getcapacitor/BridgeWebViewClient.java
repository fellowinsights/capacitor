package com.getcapacitor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BridgeWebViewClient extends WebViewClient {
  private Bridge bridge;

  public BridgeWebViewClient(Bridge bridge) {
    this.bridge = bridge;
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    Bridge.inject(view, bridge.getJSInjector());
    super.onPageStarted(view, url, favicon);
  }

  @Override
  public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    if (isReload) {
      Bridge.inject(view, bridge.getJSInjector());
    }
    super.doUpdateVisitedHistory(view, url, isReload);
  }

  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    return bridge.getLocalServer().shouldInterceptRequest(request);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    Uri url = request.getUrl();
    return bridge.launchIntent(url);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    return bridge.launchIntent(Uri.parse(url));
  }
}
