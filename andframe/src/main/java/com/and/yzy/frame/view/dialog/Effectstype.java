package com.and.yzy.frame.view.dialog;



public enum  Effectstype {
	/**
	 * 正常显示
	 */
    Fadein(FadeIn.class),
   
    
    /**
     * 全屏缩小显示
     */
    Fall(Fall.class),
  
    /**
     * 抖动效果显示
     */
    Shake(Shake.class),

    /**
     * 3D底部反转显示
     */
    RotateBottom(RotateBottom.class);

    
    private Class effectsClazz;

    Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
