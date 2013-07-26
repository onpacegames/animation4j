package com.gemserk.animation4j.examples;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JEditorPane;

import com.gemserk.animation4j.animations.events.AnimationHandlerManager;
import com.gemserk.animation4j.interpolator.function.InterpolationFunction;
import com.gemserk.animation4j.interpolator.function.InterpolationFunctions;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.componentsengine.java2d.Java2dGameAdapter;
import com.gemserk.componentsengine.java2d.input.KeyboardInput;
import com.gemserk.componentsengine.java2d.input.MouseInput;
import com.gemserk.componentsengine.java2d.render.CurrentGraphicsProvider;
import com.gemserk.componentsengine.java2d.render.Graphics2dHelper;
import com.gemserk.componentsengine.java2d.render.Java2dImageRenderObject;
import com.gemserk.componentsengine.java2d.render.Java2dRenderer;
import com.google.inject.Inject;

public class Example3 extends Java2dGameAdapter {

	@Inject
	KeyboardInput keyboardInput;

	@Inject
	MouseInput mouseInput;

	@Inject
	AnimationHandlerManager animationHandlerManager;

	BufferedImage houseImage;

	@Inject
	Java2dRenderer java2dRenderer;

	@Inject
	CurrentGraphicsProvider currentGraphicsProvider;
	
	@Inject
	Graphics2dHelper graphics2dHelper;

	Transition<Color> colorTransition;

	boolean mouseInside = false;

	JEditorPane textPane;

	JEditorPane creditsPane;

	private Color houseColor;

	@Override
	public void init() {
		
		houseImage = ImageUtils.load("house-128x92.png");
		
		creditsPane = new JEditorPane("text/html", new FileHelper("license-lostgarden.html").read()) {
			private static final long serialVersionUID = 1L;
			{
				setSize(600, 40);
				setEditable(false);
				setOpaque(false);
			}
		};
		textPane = new JEditorPane("text/html", new FileHelper("example3.html").read()) {
			private static final long serialVersionUID = 1L;
			{
				setSize(600, 240);
				setEditable(false);
				setOpaque(false);
			}
		};
		InterpolationFunction[] functions = { InterpolationFunctions.linear() };

		// Creates a Color transition using a color interpolator with a linear interpolation function.

		houseColor = new Color(0.3f, 0.3f, 0.8f, 1f);
		colorTransition = Transitions.transition(houseColor, new ColorConverter()) //
				.functions(functions) //
				.build();
	}

	@Override
	public void render(Graphics2D graphics) {
		graphics.setBackground(java.awt.Color.black);
		graphics.clearRect(0, 0, 800, 600);
		currentGraphicsProvider.setGraphics(graphics);

		{
			// render the image using the color of the transition
			java.awt.Color color = new java.awt.Color(houseColor.r, houseColor.g, houseColor.b, houseColor.a);
			java2dRenderer.render(new Java2dImageRenderObject(1, houseImage, 320, 340, 1, 1, 0f, color));
		}

		{
			// render texts in the screen
			graphics2dHelper.pushTransform();
			graphics.translate(40, 20);
			textPane.paint(graphics);
			graphics2dHelper.popTransform();
		}
		
		{
			graphics2dHelper.pushTransform();
			graphics.translate(20, 400);
			creditsPane.paint(graphics);
			graphics2dHelper.popTransform();
		}
		
	}

	@Override
	public void update(int delta) {

		colorTransition.update(0.001f * delta);

		Point mousePosition = mouseInput.getPosition();
		if (new Rectangle(320 - 64, 340 - 46, 128, 92).contains(mousePosition.x, mousePosition.y)) {
			if (!mouseInside) {
				mouseInside = true;

				// when the mouse is over the image, we set the color to white
				colorTransition.start(0.6f, new Color(1f, 1f, 1f, 1f));
			}
		} else {
			if (mouseInside) {
				mouseInside = false;

				// when the mouse left the image, we set again the color to the previous color.
				colorTransition.start(0.6f, new Color(0.3f, 0.3f, 0.8f, 1f));
			}
		}

	}

}