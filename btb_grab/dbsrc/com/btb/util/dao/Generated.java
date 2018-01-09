package com.btb.util.dao;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Generated   {
	public enum GeneratedType{ time,uuid,dbBase};
	public GeneratedType value() default GeneratedType.time;
}
