package com.brett.voxel.inventory;

import java.io.Serializable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.brett.IKeyState;
import com.brett.KeyMaster;
import com.brett.renderer.gui.GUIRenderer;
import com.brett.renderer.gui.UIMaster;
import com.brett.tools.EventQueue;
import com.brett.voxel.tools.IEventListener;
import com.brett.voxel.world.items.ItemStack;

/**
*
* @author brett
* @date Mar. 15, 2020
*/

public class Hotbar extends Inventory implements IEventListener, IKeyState, Serializable {

	// ever wondered what this is for? its for serialization.
	private static final long serialVersionUID = 8751794979515722648L;

	public static int hoverTexture = 0;
	
	private int selectedSlot = 0;
	private Inventory pl;
	private GUIRenderer rend;
	private float x;
	
	public Hotbar(Inventory pl, UIMaster ui) {
		super(694, "hotbar");
		this.pl = pl;
		this.rend = ui.getRenderer();
		float sizeX = 64*9;
		this.x = Display.getWidth()/2 - sizeX/2;
		// register slot stuff
		for (int i = 0; i < 9; i++) {
			super.addSlot(new Slot(x + (i*64), Display.getHeight()-70, 64, 64));
		}
		super.loadInventory();
		EventQueue.regiserEvent(0, this);
		KeyMaster.registerKeyRequester(this);
	}
	
	public ItemStack getItemSelected() {
		return getSlots().get(selectedSlot).getItemStack();
	}
	
	public Slot getSelectedSlot() {
		return getSlots().get(selectedSlot);
	}
	
	@Override
	public void update() {
		if (this.isEnabled()) {
			// renderes are selected texture on the selected slot
			rend.startrender();
			rend.render(hoverTexture, x + (selectedSlot*64), Display.getHeight()-70, 64, 64);
			rend.stoprender();
		}
		if (pl.getEnabled())
			super.update();
		else {
			float delta = Mouse.getDWheel();
			// moves the selected slot up and down.
			if (delta < 0 ) {
				selectedSlot++;
				if (selectedSlot >= 9)
					selectedSlot = 0;
			}
			if (delta > 0) {
				selectedSlot--;
				if (selectedSlot < 0)
					selectedSlot = 8;
			}
		}
	}

	@Override
	public void event() {
		super.enable();
	}

	@Override
	public void onKeyPressed() {
		
	}

	@Override
	public void onKeyReleased() {
		
	}
	
}
