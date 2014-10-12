class CreateMeasurements < ActiveRecord::Migration
  def change
    create_table :measurements do |t|
      t.integer :temperature
      t.timestamps
    end
  end
end
