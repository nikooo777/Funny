package ch.supsi.compinterp.funny;

import java.util.ArrayList;

public class SequenceNode extends Node
{

	private ArrayList<Node> expressions;

	public SequenceNode(ArrayList<Node> expressions)
	{
		this.expressions = expressions;
	}

}
