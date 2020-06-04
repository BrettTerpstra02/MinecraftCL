package com.brett.voxel.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import com.brett.renderer.Loader;
import com.brett.renderer.MasterRenderer;
import com.brett.renderer.datatypes.RawModel;
import com.brett.tools.Maths;
import com.brett.tools.obj.OBJLoader;
import com.brett.voxel.renderer.shaders.VEntityShader;
import com.brett.voxel.world.VoxelWorld;
import com.brett.voxel.world.entity.VEntity;
import com.brett.world.cameras.Camera;

/**
*
* @author brett
* @date Apr. 10, 2020
*/

public class VEntityRenderer {
	
	private VoxelWorld world;
	private List<VEntity> ents = new ArrayList<VEntity>();
	private VEntityShader shader;
	private Camera camera;
	public RawModel player;
	public int texture;
	
	public VEntityRenderer(VoxelWorld world, MasterRenderer renderer, Camera camera, Loader loader) {
		this.world = world;
		this.shader = new VEntityShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(renderer.getProjectionMatrix());
		this.shader.stop();
		this.camera = camera;
		player = loader.loadToVAO(OBJLoader.loadOBJ("player"));
		texture = loader.loadSpecialTexture("player");
	}
	
	public void render() {
		shader.start();
		shader.loadViewMatrix(camera);
		/*for (VEntity texture : ents) {
			texture.update();
			RawModel mod = texture.getModel();
			GL30.glBindVertexArray((int)mod.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			shader.loadTranslationMatrix(Maths.createTransformationMatrix(new Vector3f()));
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture().getID());
			GL11.glDrawElements(0x4, (int)mod.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}*/
		GL30.glBindVertexArray(player.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Iterator<Entry<Integer, Vector3f>> plyIt = VoxelWorld.localClient.clients.entrySet().iterator();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		while (plyIt.hasNext()) {
			shader.loadTranslationMatrix(Maths.createTransformationMatrix(plyIt.next().getValue()));
			GL11.glDrawElements(0x4, player.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void spawnEntity(VEntity e) {
		e.onEntitySpawned(world);
		ents.add(e);
	}
	
	public void deleteEntity(VEntity e) {
		ents.remove(e);
	}
	
}
