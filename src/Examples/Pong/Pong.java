package Examples.Pong;

import Core.Pelm;
import Core.Subscription;
import Examples.Pong.Message.Message;
import Examples.Pong.Model.*;
import Examples.Pong.Utils.Vec2;
import processing.core.PFont;

import Examples.Pong.Message.*;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static Examples.Pong.Model.Ball.*;
import static Examples.Pong.Model.Paddle.PaddleHeight;
import static Examples.Pong.Model.Paddle.PaddleWidth;

public class Pong extends Pelm<Model, Message>
{
    private static Paddle initPaddle()
    {
        return new Paddle(0.5f, EnumSet.noneOf(Direction.class), 0);
    }

    private static Ball initBall()
    {
        return new Ball(new Vec2(0.5f, 0.5f), new Vec2(InitialBallSpeed, 0f), InitialBallSpeed);
    }

    public Pong()
    {
        this(new Model(initPaddle(), initPaddle(), initBall()));
    }

    private Pong(Model model)
    {
        super(model);
    }

    private final List<Subscription<Message>> subs = Subscriptions.subscriptions(millis());

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(Model model)
    {
        return subs.stream();
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    protected void onSetup() {
        PFont font = createFont("PongFont\\pong-score.ttf", 128);

        textFont(font);

        textSize(100f);

        // Looks funky with this
        strokeWeight(10f);
    }

    @Override
    protected void view(Model model)
    {
        background(120);

        // Centre line
        line(0f, 0.5f * height, width, 0.5f * height);

        Vec2 leftPlayerCorner  = model.leftPlayer().topLeftCorner(Player.Left).toScreenSpace(width, height);
        Vec2 rightPlayerCorner = model.rightPlayer().topLeftCorner(Player.Right).toScreenSpace(width, height);

        // Draw left paddle
        rect(leftPlayerCorner.x(),
                leftPlayerCorner.y(),
                PaddleWidth * width,
                PaddleHeight * height);

        // Draw right paddle
        rect(rightPlayerCorner.x(),
                rightPlayerCorner.y(),
                PaddleWidth * width,
                PaddleHeight * height);

        var ballCorner = model.ball().topLeftCorner().toScreenSpace(width, height);

        // Draw ball
        rect(ballCorner.x(),
                ballCorner.y(),
                BallWidth * width,
                BallHeight * height);

        // Score!
        text(model.leftPlayer().score(), 0.4f * displayWidth, 0.2f * displayHeight);
        text(model.rightPlayer().score(), 0.6f * displayWidth, 0.2f * displayHeight);
    }

    @Override
    protected Model update(Message message, Model model)
    {
        return switch (message)
        {
            case None _ -> model;
            case AddDirection b -> switch (b.player())
            {
                case Left -> model.withLeftDirection(b.direction());
                case Right -> model.withRightDirection(b.direction());
            };
            case RemoveDirection b -> switch (b.player())
            {
                case Left -> model.withoutLeftDirection(b.direction());
                case Right -> model.withoutRightDirection(b.direction());
            };
            case Interval _ -> model
                    .movePaddles()
                    .moveBall()
                    .handlePaddleCollisions()
                    .handleWallCollisions()
                    .handleScoring();
        };
    }
}
