/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.util.ArrayList;

class NodeList
{

	private final ArrayList<Node> nodes;

	NodeList(ArrayList<Node> nodes)
	{
		this.nodes = nodes;
	}

	ArrayList<Node> asArrayList()
	{
		return this.nodes;
	}

}