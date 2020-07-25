package physics.collisionDetection.collisionData;

public class ContactData implements Cloneable
{
	public int num = 0;
	
	public ContactPoint[] contacts = new ContactPoint[4];
	
	public void addContact(ContactPoint contact)
	{
		if (num < 4)
		{
			contacts[num] = contact;
			num ++;
		}
		else
		{
			contacts[0] = contact;
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ContactData c = (ContactData) super.clone();
		c.contacts = new ContactPoint[4];
		c.contacts[0] = (ContactPoint) contacts[0].clone();
		c.contacts[1] = (ContactPoint) contacts[1].clone();
		c.contacts[2] = (ContactPoint) contacts[2].clone();
		c.contacts[3] = (ContactPoint) contacts[3].clone();
		return super.clone();
	}
}
