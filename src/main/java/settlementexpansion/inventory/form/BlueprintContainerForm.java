package settlementexpansion.inventory.form;

import necesse.engine.Screen;
import necesse.engine.network.client.Client;
import necesse.engine.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.GameBackground;
import necesse.gfx.forms.ContainerComponent;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormFlow;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.lists.FormStringSelectList;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.containerComponent.ContainerFormSwitcher;
import necesse.gfx.forms.presets.containerComponent.settlement.SettlementObjectStatusFormManager;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.gameTooltips.*;
import necesse.gfx.ui.ButtonColor;
import necesse.level.gameLogicGate.entities.SoundLogicGateEntity;
import settlementexpansion.inventory.container.BlueprintContainer;
import settlementexpansion.map.preset.BlueprintHelper;
import settlementexpansion.registry.ObjectModRegistry;

import java.awt.*;
import java.util.Arrays;

public class BlueprintContainerForm<T extends BlueprintContainer> extends ContainerFormSwitcher<T> {
    public Form buildForm;
    public SettlementObjectStatusFormManager settlementObjectFormManager;
    public FormStringSelectList woodTypes;
    public FormStringSelectList wallTypes;
    public FormLocalTextButton buildButton;

    public BlueprintContainerForm(Client client, T container) {
        super(client, container);
        int height = 60;
        if (container.objectEntity.getPreset().canChangeWalls) height += 60;
        if (container.objectEntity.getPreset().isFurnished()) height += 60;
        this.buildForm = this.addComponent(new Form(400, height));
        FormFlow heightFlow = new FormFlow(5);
        this.buildForm.addComponent(heightFlow.next(new FormLocalLabel(container.objectEntity.getObject().getLocalization(), new FontOptions(16), 0, this.buildForm.getWidth() / 2, 5), 5));

        if (container.objectEntity.getPreset().canChangeWalls) {
            this.wallTypes = this.buildForm.addComponent(heightFlow.next(new FormStringSelectList(this.buildForm.getWidth() / 4, this.buildForm.getHeight() / 2 - 40, 200, 49, BlueprintHelper.wallTypes)));
            this.wallTypes.setSelected(0);
            this.wallTypes.onSelect((e) -> {
                container.setWallType.runAndSend(e.str);
            });
        }

        if (container.objectEntity.getPreset().isFurnished()) {
            this.woodTypes = this.buildForm.addComponent(heightFlow.next(new FormStringSelectList(this.buildForm.getWidth() / 4, this.buildForm.getHeight() / 2 - 40, 200, 49, ObjectModRegistry.woodFurnitureTypes)));
            this.woodTypes.setSelected(1);
            this.woodTypes.onSelect((e) -> {
                container.setWoodType.runAndSend(e.str);
            });
        }

        this.buildButton = this.buildForm.addComponent(heightFlow.next(new FormLocalTextButton("ui", "blueprintbuildconfirm", this.buildForm.getWidth() / 4, this.buildForm.getHeight() / 2, 200, FormInputSize.SIZE_24, ButtonColor.BASE)));
        this.buildButton.onClicked((e) -> {
            container.buildAction.runAndSend();
        });

        this.settlementObjectFormManager = container.settlementObjectManager.getFormManager(this, this.buildForm, client);
        this.makeCurrent(this.buildForm);
        this.updateBuildActive();
    }

    public boolean shouldOpenInventory() {
        return true;
    }

    public void onWindowResized() {
        super.onWindowResized();
        ContainerComponent.setPosFocus(this.buildForm);
        this.settlementObjectFormManager.onWindowResized();
    }

    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        this.settlementObjectFormManager.updateButtons();
        updateBuildActive();
        super.draw(tickManager, perspective, renderBox);
        if (this.buildButton.isHovering()) {
            ListGameTooltips tooltips = new ListGameTooltips();
            tooltips.add(this.container.objectEntity.getPreset().getRecipe().getTooltip(this.client.getPlayer()));
            Screen.addTooltip(tooltips, GameBackground.getItemTooltipBackground(), TooltipLocation.FORM_FOCUS);
        }
    }

    private void updateBuildActive() {
        this.buildButton.setActive(this.container.objectEntity.getPreset().getRecipe().canBuild(this.client.getPlayer()));
    }
}
