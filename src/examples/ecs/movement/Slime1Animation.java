package examples.ecs.movement;

public enum Slime1Animation
{
    Attack("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Attack_without_shadow.png"),
    Death("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Death_without_shadow.png"),
    Hurt("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Hurt_without_shadow.png"),
    Idle("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Idle_without_shadow.png"),
    Run("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Run_without_shadow.png"),
    Walk("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Without_shadow/Slime1_Walk_without_shadow.png");
    
    public final String path;

    private Slime1Animation(final String path)
    {
        this.path = path;
    }
}
