# --------------------
# SwitchCompatFixed
# --------------------

-keep class android.support.v7.widget.SwitchCompat {
    private void cancelPositionAnimator();
    private void setThumbPosition(float);
}