package ca.uqac.lif.cornipickle.fiddle;

public class FiddlePair
{
	protected String m_state;
	
	protected String m_argument;
	
	public FiddlePair(String state, String argument)
	{
		super();
		m_state = state;
		m_argument = argument;
	}
	
	public String getState()
	{
		return m_state;
	}
	
	public String getArgument()
	{
		return m_argument;
	}
}
