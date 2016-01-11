package noki.moreturtles.proxy;

import noki.moreturtles.MoreTurtlesData;


/**********
 * @class ProxyServer
 *
 * @description サーバ用proxyクラス。
 * @description_en Proxy class for Server.
 */
public class ProxyServer implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerRenderers() {
		
		MoreTurtlesData.renderID_extendedBlocks = -1;
		
	}
	
	@Override
	public void registerItemJsonPre() {
		
	}
	
	@Override
	public void registerBlockJsonPre() {
		
	}
	
	@Override
	public void registerItemJson() {
		
	}
	
	@Override
	public void registerBlockJson() {
		
	}
	
	@Override
	public void registerSidedEvent() {
		
	}
	
}
