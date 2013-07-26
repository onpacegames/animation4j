package com.gemserk.animation4j.interpolator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.awt.Color;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.Vector2f;
import com.gemserk.animation4j.Vector2fConverter;
import com.gemserk.animation4j.converters.TypeConverter;

public class GenericInterpolatorTest {

	private class colorConverter implements TypeConverter<Color> {
		@Override
		public float[] copyFromObject(Color object, float[] x) {
			float[] xCopy = x;
			if (xCopy == null) {
				xCopy = new float[variables()];
			}
			object.getRGBComponents(xCopy);
			return xCopy;
		}

		@Override
		public Color copyToObject(Color object, float[] x) {
			// as color is immutable, it will return a new color each time.
			return new Color(x[0], x[1], x[2], x[3]);
		}

		@Override
		public int variables() {
			return 4;
		}
	}

	@Test
	public void test() {

		GenericInterpolator<Color> arrayInterpolator = new GenericInterpolator<Color>(new colorConverter(), new FloatArrayInterpolator(4));

		Color startColor = new Color(0f, 0f, 0f, 0f);
		Color endColor = new Color(1f, 1f, 1f, 1f);

		Color result = arrayInterpolator.interpolate(startColor, endColor, 0.5f);

		assertNotNull(result);
		float[] rgb = result.getRGBComponents(null);
		assertThat(rgb[0], IsEqual.equalTo(0.5f));
		assertThat(rgb[1], IsEqual.equalTo(0.5f));
		assertThat(rgb[2], IsEqual.equalTo(0.5f));
		assertThat(rgb[3], IsEqual.equalTo(0.5f));

	}

	@Test
	public void test2() {

		FloatArrayInterpolator interpolator = new FloatArrayInterpolator(new float[4]);

		float[] a = new float[] { 0f, 0f, 0f, 0f };
		float[] b = new float[] { 1f, 1f, 1f, 1f };

		float[] out = interpolator.interpolate(a, b, 0.5f);

		assertThat(out[0], IsEqual.equalTo(0.5f));
		assertThat(out[1], IsEqual.equalTo(0.5f));
		assertThat(out[2], IsEqual.equalTo(0.5f));
		assertThat(out[3], IsEqual.equalTo(0.5f));

	}

	@Test
	public void test3() {

		// converter uses a temporary vector to return
		// WARNING: if converter is used multiple times, it will modify the vector already returned.

		TypeConverter<Vector2f> converter = new Vector2fConverter();

		GenericInterpolator<Vector2f> arrayInterpolator = new GenericInterpolator<Vector2f>(converter, new FloatArrayInterpolator(2));

		Vector2f t1 = new Vector2f(100f, 100f);
		Vector2f t2 = new Vector2f(200f, 200f);

		Vector2f result = arrayInterpolator.interpolate(t1, t2, 0.5f);
		result = arrayInterpolator.interpolate(t1, t2, 0.5f);
		result = arrayInterpolator.interpolate(t1, t2, 0.5f);
		result = arrayInterpolator.interpolate(t1, t2, 0.5f);
		result = arrayInterpolator.interpolate(t1, t2, 0.5f);

		assertNotNull(result);
		assertThat(result.x, IsEqual.equalTo(150f));
		assertThat(result.y, IsEqual.equalTo(150f));

	}

	@Test
	public void testGarbageGenerationWhenCallingGenericInterpolatorInterpolate() {

		Interpolator<Vector2f> interpolator = new GenericInterpolator<Vector2f>(new Vector2fConverter(), new FloatArrayInterpolator(2));

		System.out.println("total memory: " + Runtime.getRuntime().totalMemory());
		System.out.println("free memory before: " + Runtime.getRuntime().freeMemory());

		Vector2f a = new Vector2f(100, 100);
		Vector2f b = new Vector2f(200, 200);

		int interpolationsQuantity = 100000;

		System.out.println("running " + interpolationsQuantity + " interpolations.");

		for (int i = 0; i < interpolationsQuantity; i++) {
			interpolator.interpolate(a, b, 0.5f);
		}

		System.out.println("free memory after: " + Runtime.getRuntime().freeMemory());

	}
	
	@Test
	public void shouldModifyMutableObjectWhenInterpolating() {
		Vector2f mutableVector = new Vector2f(-500f, -200f);
		
		Interpolator<Vector2f> interpolator = new GenericInterpolator<Vector2f>(mutableVector, new Vector2fConverter());

		Vector2f a = new Vector2f(100, 100);
		Vector2f b = new Vector2f(200, 200);

		interpolator.interpolate(a, b, 0.5f);
		
		assertThat(mutableVector.x, IsEqual.equalTo(150f));
		assertThat(mutableVector.y, IsEqual.equalTo(150f));
	}

}
