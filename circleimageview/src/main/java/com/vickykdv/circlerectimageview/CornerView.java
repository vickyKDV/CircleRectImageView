package com.vickykdv.circlerectimageview;



import androidx.annotation.IntDef;

@IntDef({
        CornerView.KIRI_ATAS, CornerView.KANAN_ATAS,
        CornerView.KIRI_BAWAH, CornerView.KANAN_BAWAH
})

public @interface CornerView {
    int KIRI_ATAS = 0;
    int KANAN_ATAS = 1;
    int KIRI_BAWAH = 2;
    int KANAN_BAWAH = 3;
}
